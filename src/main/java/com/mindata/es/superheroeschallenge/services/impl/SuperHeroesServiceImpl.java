package com.mindata.es.superheroeschallenge.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.mindata.es.superheroeschallenge.config.RequestExecutionTime;
import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNoContentException;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNotFoundException;
import com.mindata.es.superheroeschallenge.models.SuperHeroe;
import com.mindata.es.superheroeschallenge.repository.SuperHeroesRepository;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;

@Service
public class SuperHeroesServiceImpl implements SuperHeroesService {

	private SuperHeroesRepository superHeroesRepository;

	public SuperHeroesServiceImpl(SuperHeroesRepository superHeroesRepository) {
		this.superHeroesRepository = superHeroesRepository;
	}

	@RequestExecutionTime
	@Cacheable("superheroes")
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

	@RequestExecutionTime
	@Cacheable("superheroes")
	@Override
	public SuperHeroesDto getSuperHeroeById(String superHeroeId) {
		SuperHeroesDto result;
		var id = Long.parseLong(superHeroeId);
		Optional<SuperHeroe> superHeroeDb = superHeroesRepository.findById(id);
		if (superHeroeDb.isPresent()) {
			var superHeroe = superHeroeDb.get();
			result = new SuperHeroesDto(superHeroe.getId(), superHeroe.getName());
		} else {
			throw new SuperHeroesNotFoundException();
		}

		return result;
	}

	@RequestExecutionTime
	@Cacheable("superheroes")
	@Override
	public List<SuperHeroesDto> getSuperHeroesByName(String name) {
		List<SuperHeroesDto> result = new ArrayList<>();
		List<SuperHeroe> superHeroesDb = superHeroesRepository.findByNameContaining(name);
		if (superHeroesDb.size() > 0) {
			superHeroesDb
					.forEach(superHeroe -> result.add(new SuperHeroesDto(superHeroe.getId(), superHeroe.getName())));
		} else {
			throw new SuperHeroesNoContentException();
		}
		return result;
	}

	@RequestExecutionTime
	@Override
	public Long createSuperHeroe(SuperHeroesDto superHeroe) {

		var newSuperHeroe = new SuperHeroe();
		newSuperHeroe.setName(superHeroe.getName());
		newSuperHeroe = superHeroesRepository.save(newSuperHeroe);

		return newSuperHeroe.getId();
	}

	@RequestExecutionTime
	@Override
	public SuperHeroesDto updateSuperHeroe(String idSuperHeroe, SuperHeroesDto newSuperHeroe) {

		var id = Long.parseLong(idSuperHeroe);
		var superHeroeDb = superHeroesRepository.findById(id);
		if (superHeroeDb.isPresent()) {
			var superHeroe = superHeroeDb.get();
			superHeroe.setName(newSuperHeroe.getName());

			superHeroe = superHeroesRepository.save(superHeroe);
			newSuperHeroe.setId(id);
		} else {
			throw new SuperHeroesNotFoundException();
		}
		return newSuperHeroe;
	}

	@RequestExecutionTime
	@Override
	public Boolean deleteSuperHeroe(String idSuperHeroe) {
		var id = Long.parseLong(idSuperHeroe);
		if (superHeroesRepository.existsById(id)) {
			superHeroesRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

}
