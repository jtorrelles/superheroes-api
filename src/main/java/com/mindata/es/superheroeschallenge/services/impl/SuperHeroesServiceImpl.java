package com.mindata.es.superheroeschallenge.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.repository.SuperHeroesRepository;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;

@Service
public class SuperHeroesServiceImpl implements SuperHeroesService{

	private SuperHeroesRepository superHeroesRepository;
	
	public SuperHeroesServiceImpl(SuperHeroesRepository superHeroesRepository) {
		this.superHeroesRepository = superHeroesRepository;
	}
	
	@Override
	public List<SuperHeroesDto> getAllSuperHeroes() {
		return null;
	}

}
