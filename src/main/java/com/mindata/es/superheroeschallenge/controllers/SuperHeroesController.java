package com.mindata.es.superheroeschallenge.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;

@RestController
@RequestMapping("/superheroes")
public class SuperHeroesController {

	private SuperHeroesService superHeroesService;

	public SuperHeroesController(SuperHeroesService superHeroesService) {
		this.superHeroesService = superHeroesService;
	}

	@GetMapping
	public List<SuperHeroesDto> getAll(@RequestParam(required = false, name = "name") Optional<String> name) {
		if (name.isPresent()) {
			return superHeroesService.getSuperHeroesByName(name.get());
		}
		return superHeroesService.getAllSuperHeroes();
	}

	@GetMapping("/{id}")
	public SuperHeroesDto getById(@PathVariable long id) {
		return superHeroesService.getSuperHeroeById(id);
	}

	@PostMapping
	public ResponseEntity<SuperHeroesDto> createSuperHeroe(@RequestBody SuperHeroesDto newSuperHeroe) {

		Long superHeroeId = superHeroesService.createSuperHeroe(newSuperHeroe);
		newSuperHeroe.setId(superHeroeId);

		String resourceLocation = new StringBuilder("/superheroes/").append(superHeroeId.toString()).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		headers.add("Location", resourceLocation);

		return new ResponseEntity<SuperHeroesDto>(newSuperHeroe, headers, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SuperHeroesDto> updateSuperHeroe(@PathVariable Long id,
			@RequestBody(required = true) SuperHeroesDto newSuperHeroe) {

		superHeroesService.updateSuperHeroe(id, newSuperHeroe);

		String resourceLocation = new StringBuilder("/superheroes/").append(id).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		headers.add("Location", resourceLocation);

		return new ResponseEntity<SuperHeroesDto>(newSuperHeroe, headers, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> updateSuperHeroe(@PathVariable Long id) {
		superHeroesService.deleteSuperHeroe(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

}
