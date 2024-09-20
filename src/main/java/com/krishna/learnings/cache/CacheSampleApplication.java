package com.krishna.learnings.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CacheSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheSampleApplication.class, args);
	}

}
