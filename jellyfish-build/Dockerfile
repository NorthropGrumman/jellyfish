FROM openjdk:8-jdk-slim

COPY build/gradlew /temp/setup/
COPY build/gradle/ /temp/setup/gradle/
COPY ./build.gradle /temp/setup/
COPY build/versions.gradle /temp/

RUN mkdir -p ~/.gradle \
 && echo 'gpr.user=tjhooper' > ~/.gradle/gradle.properties \
 && echo 'gpr.key=7a06332c0d864e3816dff433ca78aff46ab337d3' >> ~/.gradle/gradle.properties \
 && /temp/setup/gradlew build --project-dir /temp/setup \
 && rm -rf /temp/setup \
 && rm /temp/versions.gradle