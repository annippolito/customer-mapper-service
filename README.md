# Customer Mapper Service
### Service info
This service stores entities defined as:

- customerId -> int
- externalId -> string
- createdAt -> date

It expose two endpoints with a POST and GET operations.

- POST endpoint take as parameter the `customerId` and the `createdAt`, store it in a in-memory database pairing it with an externalId.
  The `externalId` is generated the service itself.
  The date is expected to be valid with format `yyyy-mm-dd` and can't be in the future.

- GET endpoint returns the `externalId` of a given `customerId`

### Requirements

- [JDK >= 11](https://adoptopenjdk.net/)
- [Maven 3](https://maven.apache.org)

### Building the service
```shell
mvn clean package
```

### Run tests
You can execute both **junit** tests than **integration** tests by:
```shell
mvn clean test
```

### Running the service locally
 
After the project is built you can run it locally:
```shell
java -jar target/customer-mapper-service-0.0.1-SNAPSHOT.jar
```
Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

### Explore and test manually the api
You can access and use the api easily by the **swagger** once the service is running
1. ```mvn spring-boot:run```
2. ```http://localhost:8080/swagger-ui/index.html#```

### Running the service in Docker

If you want run it in [Docker](https://docs.docker.com/) you can build the image and then run it

1. ```docker build -t customer-mapper-service .```
2. ```docker run -it --rm -p8080:8080 customer-mapper-service```
3. ```http://localhost:8080/swagger-ui/index.html#```
