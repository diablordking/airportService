# Metar Web Service

It is a service that continuously store a subset METAR codes for the subscribed airports. Also it make sure that data
consistent and up-to-date

### Requirements

    🔸 Maven  
    🔸 JDK 17   
    🔸 IDE Intellij IDEA

### Swagger Api Documentation

    🔸 http://localhost:11010/metarV1/swagger-ui/

### Docker Image

    🔸 https://hub.docker.com/r/diablordking/metar-service

### Installation

Clone the repo

```
git clone https://github.com/your_username_/Project-Name.git
 ```

Open the project on Intellij IDEA

```
mvn clean install

run AirportServiceApplication.java
 ```

OR You can run with Docker image

```
docker pull diablordking/metar-service

docker run -p 11010:11010 metar-service:latest
```

### Writer

Mehmet Alp Albayraktaroğlu

