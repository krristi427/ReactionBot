FROM openjdk:16-jdk-alpine
MAINTAINER krristi1234
# paste your token directly after the =
ENV TOKEN=YOUR-TOKEN-HERE
COPY build/libs/ReactionBot-0.0.1-SNAPSHOT.jar ReactionBot-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ReactionBot-0.0.1-SNAPSHOT.jar"]