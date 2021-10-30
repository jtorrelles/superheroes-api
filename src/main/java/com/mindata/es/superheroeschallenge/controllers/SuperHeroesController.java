package com.mindata.es.superheroeschallenge.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	private List<SuperHeroesDto> getAll() {
		return superHeroesService.getAllSuperHeroes();
	}

	@GetMapping
	@RequestMapping("/{id}")
	private SuperHeroesDto getById(@PathVariable Long id) {
		return superHeroesService.getSuperHeroeById(id);
	}
}
