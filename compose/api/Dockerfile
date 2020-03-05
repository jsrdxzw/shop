FROM openjdk:11-jdk
MAINTAINER ZHIWEI XU <jsrdxzw@gmail.com>

COPY foodie-api-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=dev"]