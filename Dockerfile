FROM openjdk:17
COPY ./build/libs/WeserBot-0.2.3.jar WeserBot.jar
COPY /data /data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "WeserBot.jar"]