FROM openjdk:15
VOLUME /tmp
ADD ./target/springboot-registro-0.0.1-SNAPSHOT.jar registro.jar
ENTRYPOINT ["java","-jar","/registro.jar"]