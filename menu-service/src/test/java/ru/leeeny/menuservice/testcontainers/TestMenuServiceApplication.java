package ru.leeeny.menuservice.testcontainers;

import org.springframework.boot.SpringApplication;
import ru.leeeny.menuservice.MenuServiceApplication;
import ru.leeeny.menuservice.testcontainers.config.TestcontainersConfiguration;

public class TestMenuServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MenuServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
