# Build stage
FROM maven:3.6.3-openjdk-11 as build

RUN mkdir /licenses
COPY src/main/resources/static/licenses/db2jcc_license_cisuz-1.0.jar  /licenses

RUN mvn install:install-file -Dfile=/licenses/db2jcc_license_cisuz-1.0.jar -DgroupId=com.ibm.db2 -DartifactId=db2jcc_license_cisuz -Dversion=1.0 -Dpackaging=jar

# RUN mvn install:install-file -Dfile=/Users/kadersolak/DEV/IBM/db2mac/db2java/db2jcc_license_cu.jar -DgroupId=com.ibm.db2.jcc -DartifactId=db2jcc_license_cu -Dversion=10.1.77 -Dpackaging=jar

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package 
#RUN mvn -f /home/app/pom.xml clean install 


# Package stage
FROM openjdk:11-jdk
COPY --from=build /home/app/target/ms-spring-server-0.0.1-SNAPSHOT.jar /ms-spring-server-0.0.1-SNAPSHOT.jar
#COPY src/main/resources /app/src/main/resources

#RUN cd /home/app
RUN mkdir /uploads
RUN mkdir /downloads
RUN mkdir /templates
RUN mkdir /maps
RUN mkdir /certs
RUN mkdir /licenses


RUN chgrp -R 0 /uploads && \
    chmod -R g=u /uploads

RUN chgrp -R 0 /templates && \
    chmod -R g=u /templates

RUN chgrp -R 0 /maps && \
    chmod -R g=u /maps

RUN chgrp -R 0 /certs && \
    chmod -R g=u /certs

RUN chmod -R 777 /uploads
RUN chmod -R 777 /downloads
RUN chmod -R 777 /templates
RUN chmod -R 777 /maps
RUN chmod -R 777 /certs

COPY src/main/resources/static/templates /templates
COPY src/main/resources/static/maps /maps
COPY src/main/resources/static/certs /certs
COPY src/main/resources/static/licenses/db2jcc_license_cisuz-1.0.jar  /licenses
COPY src/main/resources/static/certs/IBMInternalRootCA.crt  /usr/local/openjdk-11/jre/lib/security/cacerts

#COPY src/main/resources/static/data /data


EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-classpath","/licenses/db2jcc_license_cisuz-1.0.jar:.", "-jar","/ms-spring-server-0.0.1-SNAPSHOT.jar", "com.ibm.wfm.Application"]
#ENTRYPOINT ["java", "-jar","/ms-spring-server-0.0.1-SNAPSHOT.jar", "com.ibm.wfm.Application"]