package com.mindata.es.superheroeschallenge.services;

import java.util.List;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;

public interface SuperHeroesService {
	List<SuperHeroesDto> getAllSuperHeroes();

	SuperHeroesDto getSuperHeroeById(long superHeroeId);
}
