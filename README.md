# Simple CRUD MS

## What is it?
That's a simple demo app using Spring Boot and Aerospike.

## What does it do?
It identifies the device used to access the API and stores the device information on Aerospike.

## How can I set it?

### Prerequisites

#### Java 17+
Ensure you are running Java 17 or higher:
```sh
java --version
```

#### Environment Variables
Set the following environment variables before running the application:
```sh
SERVER_PORT=8080
DB_HOST=localhost
DB_PORT=3000
DB_NAMESPACE=device-match
LOG_LEVEL=INFO
```

#### Aerospike Document Database
Make sure you have an Aerospike database running and accessible with the correct host, port, and namespace based on the environment variables.

### Running Aerospike with a Container Tool
Instead of installing and configuring Aerospike manually, you can use Docker or Podman:

#### Docker
```sh
docker run -tid --name aerospike -e "NAMESPACE=device-match" -p 3000-3002:3000-3002 aerospike/aerospike-server
```

#### Podman
```sh
podman run -tid --name aerospike -e "NAMESPACE=device-match" -p 3000-3002:3000-3002 aerospike/aerospike-server
```

## How do I test it?

### Running Tests with Maven
Navigate to the `/ms` directory, open your CLI and run the following command:
```sh
mvn clean install
```
You will see all the unit and integration test results

### Running the App with Maven Spring Boot Plugin
#### If you have Maven available on your localhost
Make sure you have Maven v3.6.3 or higher available on you localhost
Navigate to the `/ms` directory, open your CLI and run the following command:
```sh
mvn spring-boot:run -DDB_HOST=localhost -DDB_PORT=3000 -DDB_NAMESPACE=device-match -DLOG_LEVEL=INFO
```
#### If you DON'T have Maven available on your localhost
Navigate to the `/ms` directory, open your CLI and run the following command:
```sh
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-DDB_HOST=localhost -DDB_PORT=3000 -DDB_NAMESPACE=device-match -DLOG_LEVEL=INFO -DSERVER_PORT=8080"
```

### Running Tests in an IDE
Run the test classes using your preferred IDE (For example IntelliJ, Eclipse).

### API Testing with Postman or Insomnia
1. Start the application in your preferred IDE.
2. Import the API collection found in the `/test/collection` directory into Postman or Insomnia.
3. Execute the requests to test the API endpoints.

## Available Endpoints
| Method | Endpoint                      | Description                                       |
|--------|-------------------------------|---------------------------------------------------|
| POST   | /device-match                 | Registers a new device                            |
| GET    | /device-match/{id}            | Retrieves a device by ID                          |
| GET    | /device-match?osName={osName} | Retrieves all devices by OS Name (Case Sensitive) |
| DELETE | /device-match/{id}            | Deletes a device by ID                            |

Now you're ready to use Simple CRUD MS!