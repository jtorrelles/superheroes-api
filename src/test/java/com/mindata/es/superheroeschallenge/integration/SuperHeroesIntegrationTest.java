package com.mindata.es.superheroeschallenge.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.dto.response.SuperHeroesResponse;
import com.mindata.es.superheroeschallenge.models.SuperHeroe;
import com.mindata.es.superheroeschallenge.repository.SuperHeroesRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuperHeroesIntegrationTest {

	@Value("${api.security.api-key}")
	private String apiKey;

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	int randomPort;

	@Autowired
	SuperHeroesRepository superHeroeRepository;

	@Test
	@Order(1)
	public void createSuperHeroe_OK() throws Exception {

		final String baseUrl = String.format("http://localhost:%s/superheroes", randomPort);

		SuperHeroesDto superHeroe = new SuperHeroesDto();
		superHeroe.setName("Batman");

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, apiKey);
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<SuperHeroesDto> request = new HttpEntity<>(superHeroe, headers);

		ResponseEntity<SuperHeroesDto> response = restTemplate.postForEntity(baseUrl, request, SuperHeroesDto.class);

		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertEquals(superHeroe.getName(), response.getBody().getName());

	}

	@Test
	@Order(2)
	public void updateSuperHeroe_OK() throws Exception {

		var newSuperHeroe = superHeroeRepository.save(new SuperHeroe(null, "Batman"));
		final String baseUrl = String.format("http://localhost:%s/superheroes/%s", randomPort, newSuperHeroe.getId());

		SuperHeroesDto superHeroe = new SuperHeroesDto();
		superHeroe.setName("Superman");

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, apiKey);
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<SuperHeroesDto> request = new HttpEntity<>(superHeroe, headers);
		restTemplate.put(baseUrl, request);

		var superHeroeDb = superHeroeRepository.findById(newSuperHeroe.getId()).get();
		assertEquals(superHeroe.getName(), superHeroeDb.getName());
	}

	@Test
	@Order(3)
	public void deleteSuperHeroe_OK() throws Exception {

		var newResource = superHeroeRepository.save(new SuperHeroe(null, "Superman"));
		final String baseUrl = String.format("http://localhost:%s/superheroes/%s", randomPort, newResource.getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, apiKey);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		var result = restTemplate.exchange(baseUrl, HttpMethod.DELETE, request, HttpStatus.class);
		assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);

		var superHeroeDb = superHeroeRepository.existsById(newResource.getId());
		assertFalse(superHeroeDb);
	}

	@Test
	@Order(3)
	public void getAllSuperHeroes_OK() throws Exception {

		superHeroeRepository.save(new SuperHeroe(null, "Superman"));
		superHeroeRepository.save(new SuperHeroe(null, "Batman"));
		superHeroeRepository.save(new SuperHeroe(null, "Cat Woman"));
		superHeroeRepository.save(new SuperHeroe(null, "Wonder Woman"));

		final String baseUrl = String.format("http://localhost:%s/superheroes?size=10", randomPort);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, apiKey);
		HttpEntity<Object> request = new HttpEntity<>(headers);

		var result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, SuperHeroesResponse.class);
		var superheroes = result.getBody();

		assertEquals(result.getStatusCode(), HttpStatus.OK);
		assertEquals(superheroes.getSuperheroes().size(), superheroes.getTotalItems());
		assertNotNull(superheroes.getSuperheroes().stream()
				.filter(superHeroe -> superHeroe.getName().equals("Wonder Woman")).findFirst().get());
	}

	@Test
	@Order(4)
	public void getSuperHeroeById_OK() throws Exception {

		var newSuperHeroe = superHeroeRepository.save(new SuperHeroe(null, "Flash"));

		final String baseUrl = String.format("http://localhost:%s/superheroes/%s", randomPort, newSuperHeroe.getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, apiKey);
		HttpEntity<Object> request = new HttpEntity<>(headers);

		var result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, SuperHeroesDto.class);
		var superheroe = result.getBody();

		assertEquals(result.getStatusCode(), HttpStatus.OK);
		assertEquals(newSuperHeroe.getName(), superheroe.getName());

	}

	@Test
	@Order(5)
	public void getSuperHeroesByName_OK() throws Exception {

		superHeroeRepository.deleteAll();
		superHeroeRepository.save(new SuperHeroe(null, "Flash"));
		superHeroeRepository.save(new SuperHeroe(null, "Superman"));
		superHeroeRepository.save(new SuperHeroe(null, "Batman"));
		superHeroeRepository.save(new SuperHeroe(null, "Cat Woman"));

		final String baseUrl = String.format("http://localhost:%s/superheroes?name=sh&size=10", randomPort);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, apiKey);
		HttpEntity<Object> request = new HttpEntity<>(headers);

		var result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, SuperHeroesResponse.class);
		var superheroe = result.getBody();

		assertEquals(result.getStatusCode(), HttpStatus.OK);
		assertEquals(superheroe.getSuperheroes().size(), 1);
		assertEquals(superheroe.getTotalItems(), 1);

	}

}
