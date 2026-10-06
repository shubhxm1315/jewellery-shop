# Jewellery Shop - Spring Boot + MongoDB

## Requirements
- Java 17+
- Maven
- MongoDB running locally on port 27017

## Run
1. Start MongoDB.
2. Open this project in IntelliJ IDEA or VS Code.
3. Run:
   mvn spring-boot:run
4. Open http://localhost:8080

## Create admin
Register a normal account first. In MongoDB Compass, open database `jewellery_shop`, collection `users`, and change that user's `role` from `USER` to `ADMIN`.
Then log in and open `/admin/dashboard`.

## MongoDB
Connection is configured in `src/main/resources/application.properties`.
