package com.newsdigest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewsdigestApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsdigestApplication.class, args);
	}
}
