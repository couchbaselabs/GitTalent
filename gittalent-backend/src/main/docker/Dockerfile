FROM openjdk:8u102-jre
ARG finalName
ADD $finalName /gittalent.jar
ADD .github /root/
VOLUME /tmp
ENTRYPOINT ["java","-jar","/gittalent.jar"]
