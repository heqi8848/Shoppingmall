FROM @docker.registry.address@/java_base

RUN echo "@build.number@" > /apm-version

COPY ./entrypoint.sh entrypoint.sh
RUN chmod 755 ./entrypoint.sh

COPY ./@project.build.finalName@.jar app.jar
EXPOSE 80

ENTRYPOINT ./entrypoint.sh
