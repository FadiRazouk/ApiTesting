Here’s a sample `README.md` file for your API test project:

---

# API Testing with RestAssured

This project demonstrates how to perform API testing using RestAssured. It includes several test cases to validate authentication flows such as login, OTP validation, and user information retrieval. The tests are written in Java and use the JUnit framework.

## Prerequisites

To run this project, ensure you have the following tools installed:

- [Java 8+](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [JUnit 4+](https://junit.org/junit4/)

## Project Structure

- `src/test/java`: Contains the test cases for various API endpoints.
    - `ApiTest.java`: The main test file that covers login, OTP validation, and other edge cases.

## Test Cases

The following test cases are included:

1. **Valid Login and WhoAmI**:
    - Performs a valid login, validates OTP, and retrieves user details.

2. **Invalid Login**:
    - Attempts to log in with invalid credentials and expects a `500 Internal Server Error`.

3. **Invalid Email Format**:
    - Tests login with an invalid email format and expects a `302` status code.

4. **Expired OTP**:
    - Tests expired OTP scenarios, validating that the correct error messages are shown.

5. **WhoAmI Without Authorization**:
    - Attempts to access the `/whoami` endpoint without a valid token and expects a `401 Unauthorized` error.

6. **Login Without Email and Password**:
    - Tests login with missing email and password fields and expects validation error messages.

## Installation and Setup

1. Clone this repository:

    ```bash
    git clone git@github.com:FadiRazouk/ApiTesting.git
    cd ApiTesting
    ```

2. Install dependencies using Maven:

    ```bash
    mvn clean install
    ```

3. Run the tests:

    ```bash
    mvn test
    ```

## Usage

- The tests are written using [RestAssured](https://rest-assured.io/) for API interactions.
- Each test method covers a different API flow, focusing on authentication, validation, and error handling.


## Technologies Used

- **RestAssured**: A Java library for simplifying testing of REST APIs.
- **JUnit**: A popular Java testing framework.
- **Maven**: A build automation tool used for managing project dependencies.

## Improvements

The following improvements have been made to the tests:

- **Helper Methods**: Reusable methods for login and OTP verification to avoid redundant code.
- **Constants**: Common values like URLs, emails, passwords, and endpoints are extracted into constants for better maintainability.
- **Parameterized Tests**: Some tests are designed to handle different data sets, making them more flexible.