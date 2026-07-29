package ru.leeeny.reviewsservice;

import org.springframework.boot.SpringApplication;

public class TestReviewsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReviewsServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
