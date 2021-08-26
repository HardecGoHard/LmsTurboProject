FROM eclipse-temurin:11
RUN mkdir /opt/lms
ADD ./target/Lms-0.0.1-SNAPSHOT.jar /opt/lms
WORKDIR /opt/lms
ENTRYPOINT ["java","-jar", "Lms-0.0.1-SNAPSHOT.jar"]

