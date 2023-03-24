# base image to build a JRE
FROM openjdk:19-jdk-alpine as jdk-19

# required for strip-debug to work
RUN apk add --no-cache binutils

# Build small JRE image
RUN jlink \
 --verbose \
 --add-modules ALL-MODULE-PATH \
 --strip-debug \
 --no-man-pages \
 --no-header-files \
 --compress=2 \
 --output /customjre

# main app image
FROM alpine:latest
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=jdk-19 /customjre $JAVA_HOME

# Add app user
ARG APPLICATION_USER=appuser
RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

# Configure working directory
RUN mkdir /app && chown -R $APPLICATION_USER /app

USER 1000

COPY --chown=1000:1000 /target/*.jar /app/app.jar
WORKDIR /app

#EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]
