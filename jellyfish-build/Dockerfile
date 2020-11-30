FROM openjdk:8-jdk-slim

COPY ../gradlew /temp/setup/
COPY ../gradle/ /temp/setup/gradle/
COPY ./build.gradle /temp/setup/
COPY ../versions.gradle /temp/

RUN ./gradlew build --project-dir /temp/setup \
 && rm -rf /temp/setup \
 && rm /temp/versions.gradle