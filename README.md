# Bharat Loan Management Service

### Functional Concept
This application is for micro and small loan providers and provide a platform to manage customers, 
loan accounts, monitor EMI schedule etc.

### Tech

##### Prerequisites
- Java 11
- Gradle
- Postgres

##### Running tests
- `./gradlew clean test`

##### Running the application
- `./gradlew clean run --args="server config.yml"`

##### Running migration
- `./gradlew clean run --args="db migrate config.yml"`
