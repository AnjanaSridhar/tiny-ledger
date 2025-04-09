# tiny-ledger

## Introduction
This project provides a set of APIs to power a simple ledger. The following features are supported:
* View accounts.
* View current balance.
* View transaction history.
* Make a deposit.
* Withdraw money.

## Data storage
This project does not use a database. Instead, it uses an in-memory HashMap to simulate persistent storage for simplicity.
Mock data is loaded into the application when the relevant service or controller is instantiated, allowing the API to respond with realistic test data out of the box.

Note: To switch to a persistent database (e.g., H2, PostgreSQL), the service layer is designed in a way that makes refactoring straightforward.

## Requirements
* JDK version 17+

## Libs
* Spring Boot 3

## API Documentation
API documentation available via OpenAPI spec in openapi.yaml at the project root folder. This file can be:
* Viewed in Swagger Editor: https://editor.swagger.io
* Imported into Postman for testing.

## Build
To build the project, run:
```
./gradlew clean build
```

## Test
To run the tests, execute:
```
./gradlew test
```

## Running locally
To run the application locally, use:
```
./gradlew bootrun
```

## Future Improvements
* Authentication and Authorization: The API could be extended to include user authentication (e.g., JWT tokens) and authorisation to ensure secure access to sensitive data and operations like withdrawals.
* Database Integration: Currently, the project uses an in-memory HashMap to simulate data persistence. For a more robust solution, we could consider integrating a relational database (e.g., PostgreSQL) or an in-memory database (e.g., H2) to store user data persistently.
* Support for 'meta' Field: Add support for a meta field in the balances and transactions response. This field would help users understand when the data was last updated and provide additional metadata for API responses.  