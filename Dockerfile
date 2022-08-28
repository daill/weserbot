FROM openjdk:17
COPY ./build/libs/WeserBot-0.2.4.jar WeserBot.jar
COPY /data /data
EXPOSE 8999
ENTRYPOINT ["java", "-jar", "WeserBot.jar"]