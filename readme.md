# E-Commerce backend project

This project is built using Spring Boot 2.7 and Java 11. It incorporates various Spring modules and other technologies to create a microservices architecture.

## Architecture Overview

### Spring Boot 2.7
Spring Boot is used as the foundation framework for building the microservices. It provides a robust and flexible platform for developing Java applications with minimal setup.

### API Gateway
API Gateway is implemented to manage and route incoming requests to appropriate microservices. It serves as a single entry point to the system and provides functionalities such as authentication, rate limiting, and request routing.

### Netflix Feign
Netflix Feign is used for declarative REST client communication between microservices. It simplifies the process of interacting with other services by allowing developers to define interfaces and use annotations to describe the desired HTTP requests.

### Eureka Discovery
Eureka is employed for service discovery and registration. It allows microservices to locate and communicate with each other without hardcoding their network locations. This enables dynamic scaling and fault tolerance within the system.

### Resilience Circuit Breaker
Resilience Circuit Breaker pattern is implemented to enhance the system's resilience and fault tolerance. It helps prevent cascading failures by temporarily halting requests to a service that is experiencing issues, thereby preserving system stability.

### Keycloak for IAM Service
Keycloak is utilized for Identity and Access Management (IAM) services. It provides features such as user authentication, authorization, and centralized user management. With Keycloak, the system can ensure secure and controlled access to resources.

### Docker for Deployment
Docker containers are used for deploying and running the microservices. Docker provides lightweight, portable, and self-sufficient containers that encapsulate the application and its dependencies, ensuring consistency across different environments.

### PostgreSQL for Database
PostgreSQL is chosen as the relational database management system (RDBMS) for storing application data. It offers robust features, including ACID compliance, scalability, and extensibility, making it suitable for various types of applications.

## Deployment
The project can be deployed using Docker containers. Each microservice is packaged as a Docker image and can be deployed independently. Docker Compose or Kubernetes can be used for orchestrating and managing the containerized applications in a production environment.

## Getting Started
To get started with the project, follow these steps:

1. Clone the repository.

   ``git clone ``
2. Build the project using Maven or Gradle.
3. Configure the application properties according to your environment (e.g., database configuration, Keycloak settings).
4. Run the microservices locally or deploy them using Docker containers.
5. Test the APIs using tools like Postman or curl.

For detailed instructions on setting up and running the project, refer to the documentation provided in each microservice's README file.

## Contributors
- [Amit Kumar Gupta](https://github.com/amitzgupta001)

Feel free to contribute by submitting bug reports, feature requests, or pull requests!

## License
This project is licensed under the [MIT License](LICENSE).
