FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/spring-petclinic-1.5.1.jar spring-petclinic-1.5.1.jar
RUN sh -c 'touch /spring-petclinic-1.5.1.jar'

ENTRYPOINT ["java", "-jar","/spring-petclinic-1.5.1.jar"]
