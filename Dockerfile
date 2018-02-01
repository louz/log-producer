FROM maven:3-jdk-8 AS build-env

WORKDIR /app
COPY . /app
RUN mvn package

FROM openjdk:8-jre-alpine
COPY --from=build-env /app/target/*.jar /app.jar

ENV JAVA_OPTS=""
ENV SERVER_PORT 8080

EXPOSE ${SERVER_PORT}

HEALTHCHECK --interval=10s --timeout=3s \
	CMD curl -v --fail http://localhost:${SERVER_PORT} || exit 1

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]