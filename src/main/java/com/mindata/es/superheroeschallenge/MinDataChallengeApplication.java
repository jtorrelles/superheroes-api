package com.mindata.es.superheroeschallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MinDataChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinDataChallengeApplication.class, args);
	}

}
