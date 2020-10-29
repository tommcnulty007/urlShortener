FROM maven:3.6.3-openjdk-11
RUN mkdir /app
WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN mvn clean install
RUN find /app/target -name "*.jar" -exec mv {} /app/UrlShortener.jar \;
ENTRYPOINT ["java", "-jar", "/app/UrlShortener.jar"]
