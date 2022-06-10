FROM maven:3-openjdk-17

WORKDIR /app

COPY pom.xml ./
RUN mvn verify --fail-never

COPY src ./src
RUN mvn package

CMD java -jar target/jabs.jar