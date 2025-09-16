# Smoke test scenarios

-[ ] сборка и unit-тесты: `gradle clean build`
-[ ] подготовка main-repo: `io.github.xzima.docomagos.server.services.GitClientTest.testClonePositive`
-[ ] локальный запуск сервера: `npm run runLocalServe`
  ```shell
  export export HOSTNAME=$HOST
  npm run runLocalServe
  ```
    -[ ] проверка логов
    -[ ] проверка ui: http://localhost:4444/
-[ ] локальный запуск sync-job: `npm run runLocalSync`
    -[ ] проверка логов
-[ ] запуск сервера в докере: `docker-compose.yaml`
    -[ ] проверка логов
    -[ ] проверка ui: http://localhost:8080/
-[ ] проверка сценариев запуска sync-job: `io.github.xzima.docomagos.server.services.DockerServiceSmoke`
-[ ] проверка сценария запуска sync-job в докере: `cd serverApp/build/repo/ && git reset --hard HEAD~10`
    -[ ] контейнер стартует в единственном экземпляре
-[ ] 
