package com.mindata.es.superheroeschallenge.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class SuperHeroesIntegrationTest {

	@Autowired
	TestRestTemplate restTemplate;
	
	@Test
	public void getAllSuperHeroes_Test() {
		ResponseEntity<SuperHeroes> response = restTemplate.getForEntity("/superheroes", SuperHeroes.class);
		assertThat(response.getStatusCode()).equals(HttpStatus.OK);
	}
}
