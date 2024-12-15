FROM debian:12-slim
# install curl for install docker-compose standalone
RUN apt-get -y update; apt-get -y install curl
# download and install docker-compose
ARG DOCKER_COMPOSE_VERSION=v2.31.0
RUN curl -SL https://github.com/docker/compose/releases/download/$DOCKER_COMPOSE_VERSION/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose
# default app env variables
ENV RSOCKET_MAX_FRAGMENT_SIZE=1024
ENV KTOR_PORT=4444
ENV KTOR_REUSE_ADDRESS=true
ENV KTOR_GRACE_PERIOD_MILLIS=5000
ENV KTOR_GRACE_TIMEOUT_MILLIS=10000
ENV STATIC_UI_PATH=/static-ui
# copy app binaries
COPY ./serverApp/build/bin/linuxX64/releaseExecutable/serverApp.kexe /app.kexe
COPY ./uiApp/build/dist/js/productionExecutable $STATIC_UI_PATH

ENTRYPOINT ["/app.kexe"]
