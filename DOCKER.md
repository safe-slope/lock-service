# Lock Service Docker Guide

This document provides instructions for building and running the Lock Service application using Docker.

## Prerequisites

- Docker installed on your system
- Git (for cloning the repository)

## Building the Docker Image

To build the Docker image, run the following command from the repository root:

```bash
docker build -t lock-service:latest .
```

This will create a multi-stage Docker image that:
1. Uses Maven 3.9 with Eclipse Temurin JDK 21 to build the application
2. Packages the application into an executable JAR
3. Creates a lightweight runtime image with Eclipse Temurin JRE 21

## Running the Container

### Basic Run

To run the container with default settings:

```bash
docker run -p 8080:8080 lock-service:latest
```

The application will be accessible at `http://localhost:8080`.

### Run with Environment Variables

The application can be configured using environment variables:

```bash
docker run -p 8080:8080 \
  -e MQTT_USERNAME=your_username \
  -e MQTT_PASSWORD=your_password \
  -e HOSTNAME=lock-service-1 \
  lock-service:latest
```

### Run with Custom JVM Options

The Dockerfile includes the `JAVA_OPTS` environment variable for JVM configuration. You can override it:

```bash
docker run -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  lock-service:latest
```

## Docker Compose

For a complete setup with dependencies (e.g., MQTT broker), create a `docker-compose.yml`:

```yaml
version: '3.8'

services:
  lock-service:
    build: .
    ports:
      - "8080:8080"
    environment:
      - MQTT_USERNAME=admin
      - MQTT_PASSWORD=password
      - HOSTNAME=lock-service
    depends_on:
      - mosquitto

  mosquitto:
    image: eclipse-mosquitto:latest
    ports:
      - "1883:1883"
      - "9001:9001"
    volumes:
      - ./mosquitto.conf:/mosquitto/config/mosquitto.conf
```

Then run:

```bash
docker-compose up
```

## Image Details

- **Base Image (Build)**: `maven:3.9-eclipse-temurin-21`
- **Base Image (Runtime)**: `eclipse-temurin:21-jre-jammy`
- **Application Port**: 8080
- **Working Directory**: `/app`
- **JAR Location**: `/app/app.jar`

## Build Optimizations

The Dockerfile uses multi-stage builds to:
- Keep the final image size small by using JRE instead of JDK
- Separate build dependencies from runtime dependencies
- Cache Docker layers effectively

## Troubleshooting

### Build Fails

If the build fails, try:
- Ensuring you have a stable internet connection (Maven needs to download dependencies)
- Checking Docker has enough disk space
- Verifying Java 21 is correctly configured in the build image

### Container Won't Start

Check the logs:
```bash
docker logs <container-id>
```

Common issues:
- Port 8080 is already in use
- Missing required environment variables
- Insufficient memory allocation

### MQTT Connection Issues

Ensure the MQTT broker is accessible and the connection settings are correct:
- `MQTT_USERNAME` and `MQTT_PASSWORD` if authentication is required
- The broker URL is accessible from within the Docker network

## Security Considerations

- The application runs as the default user in the container
- Consider using Docker secrets for sensitive information like passwords
- In production, use specific version tags instead of `latest`

## Development

For development with hot-reload, you may want to mount the source code as a volume. However, this Dockerfile is optimized for production deployments.
