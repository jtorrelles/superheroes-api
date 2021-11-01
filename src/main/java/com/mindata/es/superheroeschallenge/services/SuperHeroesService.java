package com.mindata.es.superheroeschallenge.services;

import java.util.List;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;

public interface SuperHeroesService {
	List<SuperHeroesDto> getAllSuperHeroes();

	SuperHeroesDto getSuperHeroeById(long superHeroeId);

	List<SuperHeroesDto> getSuperHeroesByName(String name);

	Long createSuperHeroe(SuperHeroesDto newSuperHeroe);

	SuperHeroesDto updateSuperHeroe(Long idSuperHeroe, SuperHeroesDto superHeroe);

	Boolean deleteSuperHeroe(Long idSuperHeroe);
}
