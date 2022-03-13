package com.example.airportservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableAsync
@EnableCaching
@EnableJpaRepositories
@SpringBootApplication
public class AirportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirportServiceApplication.class, args);
    }


}
