FROM openjdk:8-jdk-slim

COPY build/gradlew /temp/setup/
COPY build/gradle/ /temp/setup/gradle/
COPY ./build.gradle /temp/setup/
COPY build/versions.gradle /temp/

RUN /temp/setup/gradlew build --project-dir /temp/setup \
 && rm -rf /temp/setup \
 && rm /temp/versions.gradle