FROM debian:12-slim
# build args
ARG APP_BUILD_DOCKER_COMPOSE_VERSION=v2.31.0
# labels
#LABEL org.opencontainers.image.created=2017-08-28T09:24:41Z
#LABEL org.opencontainers.image.authors=
LABEL org.opencontainers.image.url="https://github.com/xzima/doco-magos"
#LABEL org.opencontainers.image.documentation=
#LABEL org.opencontainers.image.source=
#LABEL org.opencontainers.image.version=$APP_BUILD_VERSION
#LABEL org.opencontainers.image.revision=
LABEL org.opencontainers.image.vendor="Alex Zima(xzima@ro.ru)"
LABEL org.opencontainers.image.licenses="Apache-2.0"
#LABEL org.opencontainers.image.ref.name=
LABEL org.opencontainers.image.title="doco-magos"
LABEL org.opencontainers.image.description="Docker Compose management and gitops system"
#LABEL org.opencontainers.image.base.digest=
#LABEL org.opencontainers.image.base.name=
# install curl for install docker-compose standalone
RUN apt-get -y update; apt-get -y install curl
# download and install docker-compose
RUN curl -SL https://github.com/docker/compose/releases/download/$APP_BUILD_DOCKER_COMPOSE_VERSION/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose
# default app env variables
ENV LOGGING_LEVEL=INFO
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
