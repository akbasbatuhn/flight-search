package com.amadeus.flightsearch;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		info = @Info(
				title = "Airport REST API Documentation",
				description = "Search Flight REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Batuhan Akbas",
						email = "batuhanakbas011@gmail.com"
				),
				license = @License(
						name = "Apache 2.0"
				)
		)
)
@EnableScheduling
@SpringBootApplication
public class FlightSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightSearchApplication.class, args);
	}

}
