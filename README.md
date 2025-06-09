# Beverage Shopping Application

## Table of Contents
- [Beverage Shopping Application](#beverage-shopping-application)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Technologies Used](#technologies-used)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Setup](#setup)
  - [GCP Deployment](#gcp-deployment)
    - [Creating a new project](#creating-a-new-project)
    - [Creating a service account](#creating-a-service-account)
    - [Setting up Google Cloud CLI](#setting-up-google-cloud-cli)
    - [Enabling required APIs](#enabling-required-apis)
    - [Build and deploy](#build-and-deploy)
  - [Running Tests](#running-tests)
  - [Project Structure](#project-structure)
  - [License](#license)

## Overview
The Beverage Shopping Application is a web-based platform that allows users to browse and purchase beverages. It includes features such as viewing product details, adding items to a cart, and placing orders.

## Technologies Used
- **Java**: Programming language
- **Spring Boot**: Framework for building the application
- **JUnit**: Testing framework
- **Mockito**: Mocking framework for tests
- **H2 Database**: In-memory database for testing

```
cd existing_repo
git remote add origin <your-repository-url>
git branch -M main
git push -uf origin main
```

## Getting Started

### Prerequisites
- Java 21
- Gradle

### Setup
1. Clone the repository:
   ```bash
   git clone <your-repository-url>
   cd beverage-shopping
   ```
2. Build the project:
   ```bash
   ./gradlew build
   ```
3. Run the application:
   ```bash
   ./gradlew appRun
   ```
4. Run the data seeding:
   ```bash
   ./gradlew seedDatabase
   ```
5. Run the application with data seeding:
   ```bash
   ./gradlew bootRun
   ```

## GCP Deployment

### Creating a new project
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Click on the project dropdown and select "New Project".
3. Enter the project name and other details, then click "Create".

### Creating a service account
1. In the Google Cloud Console, navigate to "IAM & Admin" > "Service Accounts".
2. Click "Create Service Account".
3. Enter a name and description for the service account.
4. Assign the following roles:
   - App Engine Admin
   - Cloud Build Service Account
   - Cloud SQL Admin
   - Storage Admin
5. Click "Done".

### Setting up Google Cloud CLI
1. Install the [Google Cloud SDK](https://cloud.google.com/sdk/docs/install).
2. Initialize the SDK:
   ```bash
   gcloud init
   ```
3. Set the project:
   ```bash
   gcloud config set project [PROJECT_ID]
   ```

### Enabling required APIs
Enable the necessary APIs using the following command:
```bash
gcloud services enable appengine.googleapis.com cloudbuild.googleapis.com storage.googleapis.com
```

### Build and deploy
1. Build the project:
   ```bash
   ./gradlew clean build
   ```
2. Deploy the application:
   ```bash
   gcloud app deploy staging/app.yml
   ```

## Running Tests
To run the tests, use the following command:
```bash
./gradlew test
```

## Project Structure
- `src/main/java`: Contains the main application code
  - `controller`: Handles HTTP requests and responses
  - `service`: Contains business logic
  - `repository`: Interfaces for data access
  - `model`: Entity classes
- `resources`: Contains static resources and templates
- `src/test/java`: Contains test cases


## License
This project is licensed under the MIT License.

