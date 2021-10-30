package com.mindata.es.superheroeschallenge.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public List<SuperHeroesDto> getAll() {
		return superHeroesService.getAllSuperHeroes();
	}

	@GetMapping
	@RequestMapping("/{id}")
	public SuperHeroesDto getById(@PathVariable long id) {
		return superHeroesService.getSuperHeroeById(id);
	}

	@GetMapping("/find")
	public Object getSuperHeroesByName(@RequestParam(required = true, name = "name") String name) {
		return superHeroesService.getSuperHeroesByName(name);
	}
}
