# Bank Account API - Assignment


## Problem Description

We are a bank and would like to modernize our Account Management system in our Retail
Banking. We would like to use Micro Service architecture to replace the monolith application.
The system is supposed to contain the following functionalities:

* ~~Create a Savings Account for the Customer~~
* ~~Deposit money to Account~~
* ~~Withdraw money from Account~~
* ~~Read available balance~~
* ~~List last 10 transactions of the Account~~


## Requirements

You are expected to create a solution that exposes REST APIs for the mentioned
functionalities by using a test-driven approach in a publicly shared GitHub repository.
The REST API must be able to handle exceptions such as invalid input, as well as business
validations from a common-sense perspective.
You should use git commit messages to show your thoughts and decisions during the
development phase.


## Technical Requirements

* Java 17
* Junit 5
* Spring-boot
* Gradle

## Extra questions (optional):

* How will you design/organize the micro services for your API product?
* How will you break down the business requirements into user stories?

## Run Guide:

- The API models and interfaces are generated via the build
- Contract can be found in `src/main/resources/api/contract.yml`

### 1. Build the project first
This will generate the API models, build the artifact and test the project
```console
gradlew clean build
```
This will regenerate the API models only
```console
gradlew clean compileJava
```

### 2. Start as a standard Spring Boot application and
1. [Try the API via the Swagger UI](http://localhost:8080/swagger-ui/index.html)
2. Import `src/main/resources/api/contract.yml` into Postman

