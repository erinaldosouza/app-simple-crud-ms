# Simple CRUD MS

## What is it?
That's a simple demo app using Spring Boot and Aerospike.

## What does it do?
It identifies the device used to access the API and records it on Aerospike.

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
Navigate to the `/ms` directory and run:
```sh
mvn clean install
```

### Running Tests in an IDE
Run the test classes using your preferred IDE (For example IntelliJ, Eclipse).

### API Testing with Postman or Insomnia
1. Start the application in your preferred IDE.
2. Import the API collection found in the `/test` directory into Postman or Insomnia.
3. Execute the requests to test the API endpoints.

## Available Endpoints
| Method | Endpoint                      | Description                      |
|--------|-------------------------------|----------------------------------|
| POST   | /device-match                 | Registers a new device           |
| GET    | /device-match/{id}            | Retrieves a device by ID         |
| GET    | /device-match?psName={osName} | Retrieves all devices by OS Name |
| DELETE | /device-match/{id}            | Deletes a device by ID           |

Now you're ready to use Simple CRUD MS!