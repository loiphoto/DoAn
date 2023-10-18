# Bước 1: Build maven project
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Bước 2: Copy file jar và chạy ứng dụng
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/DoAn-0.0.1-SNAPSHOT.jar /usr/local/lib/my-app.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/my-app.jar"]