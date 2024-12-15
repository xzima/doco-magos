package io.github.xzima.docomagos.ui.states

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.xzima.docomagos.client.DockerComposeApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class DCProjectsListViewModel(
    private val dockerComposeApiService: DockerComposeApiService,
) : ViewModel() {
    init {
        onLaunch()
    }

    private var projectsListState = MutableStateFlow(emptyList<ProjectItem>())
    val projectsList: StateFlow<List<ProjectItem>> get() = projectsListState

    fun onLaunch() {
        viewModelScope.launch {
            val data = dockerComposeApiService.listProjects().projects.map {
                ProjectItem(it.name, it.status)
            }
            projectsListState.value = data
        }
    }

    data class ProjectItem(
        val name: String,
        val status: String,
    )
}
