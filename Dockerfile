FROM openjdk:17
ADD ./build/libs/WeserBot-0.2.1.jar WeserBot.jar
ENTRYPOINT ["java", "-jar", "WeserBot.jar"]