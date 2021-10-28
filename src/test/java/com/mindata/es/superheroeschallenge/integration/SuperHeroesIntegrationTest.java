package com.mindata.es.superheroeschallenge.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class SuperHeroesIntegrationTest {

	@Autowired
	TestRestTemplate restTemplate;
	
	@Test
	public void getAllSuperHeroes_Test() {
		ResponseEntity<SuperHeroesDto> response = restTemplate.getForEntity("/superheroes", SuperHeroesDto.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
}
