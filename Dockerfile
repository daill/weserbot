FROM openjdk:16
ADD ./build/libs/WeserBot-0.2.jar WeserBot.jar
ENTRYPOINT ["java", "-jar", "WeserBot.jar"]