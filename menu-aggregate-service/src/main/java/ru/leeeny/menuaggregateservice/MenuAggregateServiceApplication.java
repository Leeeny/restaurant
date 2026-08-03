package ru.leeeny.menuaggregateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class MenuAggregateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuAggregateServiceApplication.class, args);
	}

}
