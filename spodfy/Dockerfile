FROM openjdk:8-jdk-alpine
MAINTAINER mrmoralesu@gmail.com
VOLUME /tmp
RUN mkdir /work
COPY . /work
WORKDIR /work
RUN dos2unix gradlew
RUN /work/gradlew build
RUN mv /work/build/libs/*.jar /work/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/work/app.jar"]