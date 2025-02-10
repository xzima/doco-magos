package io.github.xzima.docomagos.server.services

import TestCreator
import dev.mokkery.answering.calls
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.matching
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.services.impl.DockerComposeServiceImpl
import io.github.xzima.docomagos.server.services.models.SyncStackPlan
import kotlinx.coroutines.*
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

class DockerComposeServiceTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private val dockerComposeClient = mock<DockerComposeClient>()
    private val repoService = mock<RepoService>()
    private lateinit var dockerComposeService: DockerComposeService

    @BeforeTest
    fun setup() {
        dockerComposeService = DockerComposeServiceImpl(
            dockerComposeClient = dockerComposeClient,
            repoService = repoService,
        )
    }

    @AfterTest
    fun tearDown() {
        verifyNoMoreCalls(dockerComposeClient, repoService)
        resetCalls(dockerComposeClient, repoService)
        resetAnswers(dockerComposeClient, repoService)
    }

    @Test
    fun testExecuteSyncPlan(): Unit = runBlocking {
        // GIVEN
        everySuspend { dockerComposeClient.down(any()) } calls {
            val manifestPath = it.arg<Path>(0)
            if ("err" in manifestPath.toString()) {
                throw RuntimeException("exception with $manifestPath")
            }
        }
        everySuspend { dockerComposeClient.up(any(), any(), any(), any()) } calls {
            val manifestPath = it.arg<Path>(0)
            if ("err" in manifestPath.toString()) {
                throw RuntimeException("exception with $manifestPath")
            }
        }
        everySuspend { repoService.getEnvs(any(), true) } calls {
            val envsPath = it.arg<List<Path>>(0)
            if (envsPath.any { "err" in it.toString() }) {
                throw RuntimeException("exception with envs")
            }
            envsPath.associate { it.name to it.segments.size.toString() }
        }

        val syncPlan = SyncStackPlan(
            toDown = mutableListOf(
                TestCreator.actualProjectInfo().copy(manifestPath = manifestPathGen("da1", false)),
                TestCreator.expectedProjectInfo().copy(
                    manifestPath = manifestPathGen("de1", false),
                    envPaths = listOf(envPathGen("de1", true)),
                ),
                TestCreator.actualProjectInfo().copy(order = 1, manifestPath = manifestPathGen("da2", false)),
                TestCreator.expectedProjectInfo().copy(
                    order = 1,
                    manifestPath = manifestPathGen("de2", true),
                    envPaths = listOf(envPathGen("de2", false)),
                ),
                TestCreator.actualProjectInfo().copy(order = 10, manifestPath = manifestPathGen("da3", false)),
                TestCreator.expectedProjectInfo().copy(
                    order = 10,
                    manifestPath = manifestPathGen("de3", false),
                    envPaths = listOf(envPathGen("de3", false)),
                ),
                TestCreator.actualProjectInfo().copy(order = 3, manifestPath = manifestPathGen("da4", true)),
                TestCreator.expectedProjectInfo().copy(
                    order = 3,
                    manifestPath = manifestPathGen("de4", false),
                    envPaths = listOf(envPathGen("de4", false)),
                ),
            ),
            toUp = mutableListOf(
                TestCreator.expectedProjectInfo().copy(
                    manifestPath = manifestPathGen("ue1", false),
                    envPaths = listOf(envPathGen("ue1", false)),
                ),
                TestCreator.expectedProjectInfo().copy(
                    order = 1,
                    manifestPath = manifestPathGen("ue2", false),
                    envPaths = listOf(envPathGen("ue2", false)),
                ),
                TestCreator.expectedProjectInfo().copy(
                    order = 10,
                    manifestPath = manifestPathGen("ue3", true),
                    envPaths = listOf(envPathGen("ue3", false)),
                ),
                TestCreator.expectedProjectInfo().copy(
                    order = 300,
                    manifestPath = manifestPathGen("ue4", true),
                    envPaths = listOf(envPathGen("ue4", true)),
                ),
                TestCreator.expectedProjectInfo().copy(
                    order = 3,
                    manifestPath = manifestPathGen("ue5", false),
                    envPaths = listOf(envPathGen("ue5", false)),
                ),
                TestCreator.expectedProjectInfo().copy(
                    order = 110,
                    manifestPath = manifestPathGen("ue6", false),
                    envPaths = listOf(envPathGen("ue6", true)),
                ),
            ),
            ignored = mutableListOf(
                TestCreator.actualProjectInfo().copy(order = 404, manifestPath = manifestPathGen("ia1", true)),
                TestCreator.expectedProjectInfo().copy(
                    manifestPath = manifestPathGen("ie2", false),
                    envPaths = listOf(envPathGen("ie2", false)),
                ),
            ),
        )

        // WHEN
        dockerComposeService.executeSyncPlan(syncPlan)

        // THEN
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            dockerComposeClient.down(matching { manifestPathGen("da1", false) == it })
            dockerComposeClient.down(matching { manifestPathGen("de1", false) == it })
            dockerComposeClient.down(matching { manifestPathGen("da3", false) == it })
            dockerComposeClient.down(matching { manifestPathGen("de3", false) == it })
            dockerComposeClient.down(matching { manifestPathGen("da4", true) == it })
            dockerComposeClient.down(matching { manifestPathGen("de4", false) == it })
            dockerComposeClient.down(matching { manifestPathGen("da2", false) == it })
            dockerComposeClient.down(matching { manifestPathGen("de2", true) == it })
            repoService.getEnvs(matching { it.any { envPathGen("ue2", false) == it } }, true)
            dockerComposeClient.up(matching { manifestPathGen("ue2", false) == it }, any(), any(), any())
            repoService.getEnvs(matching { it.any { envPathGen("ue5", false) == it } }, true)
            dockerComposeClient.up(matching { manifestPathGen("ue5", false) == it }, any(), any(), any())
            repoService.getEnvs(matching { it.any { envPathGen("ue3", false) == it } }, true)
            dockerComposeClient.up(matching { manifestPathGen("ue3", true) == it }, any(), any(), any())
            repoService.getEnvs(matching { it.any { envPathGen("ue6", true) == it } }, true)
            repoService.getEnvs(matching { it.any { envPathGen("ue4", true) == it } }, true)
            repoService.getEnvs(matching { it.any { envPathGen("ue1", false) == it } }, true)
            dockerComposeClient.up(matching { manifestPathGen("ue1", false) == it }, any(), any(), any())
        }
    }

    fun manifestPathGen(name: String, isErr: Boolean): Path = if (isErr) {
        "/tmp/$name-err/compose.yml".toPath()
    } else {
        "/tmp/$name/compose.yml".toPath()
    }

    fun envPathGen(name: String, isErr: Boolean): Path = if (isErr) {
        "/tmp/$name-err/.env".toPath()
    } else {
        "/tmp/$name/.env".toPath()
    }
}
