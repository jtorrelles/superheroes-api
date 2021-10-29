package com.mindata.es.superheroeschallenge.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNoContentException;
import com.mindata.es.superheroeschallenge.models.SuperHeroe;
import com.mindata.es.superheroeschallenge.repository.SuperHeroesRepository;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;

@Service
public class SuperHeroesServiceImpl implements SuperHeroesService {

	private SuperHeroesRepository superHeroesRepository;

	public SuperHeroesServiceImpl(SuperHeroesRepository superHeroesRepository) {
		this.superHeroesRepository = superHeroesRepository;
	}

	@Override
	public List<SuperHeroesDto> getAllSuperHeroes() {
		List<SuperHeroesDto> result = new ArrayList<>();
		Iterable<SuperHeroe> response = superHeroesRepository.findAll();
		if (StreamSupport.stream(response.spliterator(), false).count() == 0) {
			throw new SuperHeroesNoContentException();
		}
		response.forEach(superHeroe -> result.add(new SuperHeroesDto(superHeroe.getId(), superHeroe.getName())));
		return result;
	}

	@Override
	public SuperHeroesDto getSuperHeroeById(long superHeroeId) {
		Optional<SuperHeroe> superHeroeDb = superHeroesRepository.findById(superHeroeId);
		return new SuperHeroesDto();
	}

}
