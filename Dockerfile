FROM openjdk:16-jdk-alpine as builder
MAINTAINER Kristi Balla

# paste your token directly after the =
ENV TOKEN=YOUR-TOKEN-HERE

COPY build/libs/ReactionBot-0.0.1-SNAPSHOT.jar ReactionBot-0.0.1-SNAPSHOT.jar
# extract the 4 layers of the jar, in order to make use of docker's caching system
RUN java -Djarmode=layertools -jar ReactionBot-0.0.1-SNAPSHOT.jar extract
FROM openjdk:16-jdk-alpine
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]