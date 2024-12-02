# JCA File Adapter

A Java Connector Architecture (JCA) implementation for file system integration with Jakarta EE applications.

## Project Structure

The project is organized into several key modules:

### Core Components

- **api**: Core APIs and interfaces for the file adapter
- **spi**: Service Provider Interface implementation
- **deployable-rar**: Deployable Resource Adapter Archive (RAR)
- **war**: Web application for testing the adapter

### Additional Components

- **file_manager**: File system management utilities
- **jca_io**: I/O operations implementation
- **RA-HelloWorld**: Example/demo implementation
- **traffic**: Traffic management module
- **twt_framework**: Framework utilities

## Technologies

- Java 8+
- Jakarta EE 10.0
- WildFly 30.0.0.Final
- Maven 3.0+

## Building the Project

1. Clone the repository
2. Build using Maven

   ```bash
   mvn clean install
   ```

## Deployment

### Resource Adapter (.rar)

1. Build the deployable RAR module

   ```bash
   cd deployable-rar
   mvn clean package
   ```

2. Deploy the generated .rar file to your WildFly server

### Web Application

1. Configure WildFly home in `war/pom.xml`

   ```xml
   <wildfly.home>path/to/your/wildfly</wildfly.home>
   ```

2. Run the Web Application

   ```bash
   cd war
   mvn wildfly:dev
   ```

Access the test interface at: `http://localhost:8080/web-app/test`

## Project Dependencies

- Jakarta EE API 10.0.0
- JavaEE API 8.0
- EJB 3.2
- Log4j 2.22.0

## Development

### Prerequisites

- JDK 8 or higher
- Maven 3.0+
- WildFly Application Server

### Building Modules

Each module can be built independently

```bash
mvn -pl api clean install
mvn -pl spi clean install
mvn -pl deployable-rar clean install
mvn -pl war clean install
```

## CI/CD

- Jenkins integration available at: `https://sbs.t-systems.com/jenkins/view/twt`
- Automated builds and deployments configured through Jenkins pipeline

## License

This project is licensed under Daimler AG

## Source Control

Repository: https://seu16.gdc-leinf01.t-systems.com/bitbucket/scm/ams4truck/jca-file-adapter.git
