# Flight Search

Sections
  - [Installation](#installation--run)
  - [Used Technologies](#used-technologies)
  - [Notes](#notes)
  - [Related Links](#links)

### Summary

The assessment consists of an API to be used for creating, searching existing “flights” and airports.

## Prerequisites

- Maven
- Docker

## Installation & Run

### Docker Compose
For docker run usage, docker image already pushed to docker.io

After cloning the project, you just need to run docker compose command:

```
$ cd flight-search
$ docker compose up
```

If you want to use your terminal you can run docker compose on **detached mode**

```
$ docker compose up -d
```

## Used Technologies

- Java 17
- Spring Boot 3.2.2
- Spring Data JPA
- Spring Security(Basic Auth)
- OpenAPI documentation
- Docker
- Docker compose
- JUnit 5
- H2 in-memory database
- Lombok

## Notes

- I added basic auth to this project. To pass auth process you can use these credentials.
```
Username: user
Password: password
```

- In Flight Search API will search flights that on same day, and before flights of given time(hour, min, sec)


- If you add return date to search, it will include available destination to departure route airport flights.


- If you want to just search there is a flight with exact time, you can search with **/api/v1/flights/exact** url


## Links

### Swagger UI
Swagger UI will be run on [this](http://localhost:8080/swagger-ui/index.html/) url:
```
http://localhost:8080/swagger-ui/index.html
```

### H2 Database
You can access H2 database console on [this](http://localhost:8080/h2-console) link
```
http://localhost:8080/h2-console
```
