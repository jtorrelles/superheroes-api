package com.mindata.es.superheroeschallenge.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mindata.es.superheroeschallenge.config.RequestExecutionTime;
import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.dto.response.SuperHeroesResponse;
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
	public SuperHeroesResponse getAllSuperHeroes(Pageable pageable) {
		List<SuperHeroesDto> listOfSuperHeroes = new ArrayList<>();
		var response = superHeroesRepository.findAll(pageable);
		if (response.getContent().size() == 0) {
			throw new SuperHeroesNoContentException();
		}
		response.getContent().stream().forEach(
				superHeroe -> listOfSuperHeroes.add(new SuperHeroesDto(superHeroe.getId(), superHeroe.getName())));

		return new SuperHeroesResponse(listOfSuperHeroes, response.getTotalElements(), response.getTotalPages(),
				response.getNumber());
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
	public SuperHeroesResponse getSuperHeroesByName(String name, Pageable pageable) {
		List<SuperHeroesDto> listOfSuperHeroes = new ArrayList<>();
		var response = superHeroesRepository.findByNameContaining(name, pageable);
		if (response.getContent().size() == 0) {
			throw new SuperHeroesNoContentException();
		}
		response.getContent().stream().forEach(
				superHeroe -> listOfSuperHeroes.add(new SuperHeroesDto(superHeroe.getId(), superHeroe.getName())));

		return new SuperHeroesResponse(listOfSuperHeroes, response.getTotalElements(), response.getTotalPages(),
				response.getNumber());
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
