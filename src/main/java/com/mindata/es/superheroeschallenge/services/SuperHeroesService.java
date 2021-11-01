package com.mindata.es.superheroeschallenge.services;

import org.springframework.data.domain.Pageable;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.dto.response.SuperHeroesResponse;

public interface SuperHeroesService {
	SuperHeroesResponse getAllSuperHeroes(Pageable pageable);

	SuperHeroesDto getSuperHeroeById(String superHeroeId);

	SuperHeroesResponse getSuperHeroesByName(String name, Pageable pageable);

	Long createSuperHeroe(SuperHeroesDto newSuperHeroe);

	SuperHeroesDto updateSuperHeroe(String idSuperHeroe, SuperHeroesDto superHeroe);

	Boolean deleteSuperHeroe(String idSuperHeroe);
}
