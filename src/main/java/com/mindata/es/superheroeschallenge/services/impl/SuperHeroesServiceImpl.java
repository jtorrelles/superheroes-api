package com.mindata.es.superheroeschallenge.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.dto.response.SuperHeroesResponse;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNoContentException;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNotFoundException;
import com.mindata.es.superheroeschallenge.models.SuperHeroe;
import com.mindata.es.superheroeschallenge.repository.SuperHeroesRepository;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SuperHeroesServiceImpl implements SuperHeroesService {

	private SuperHeroesRepository superHeroesRepository;

	public SuperHeroesServiceImpl(SuperHeroesRepository superHeroesRepository) {
		this.superHeroesRepository = superHeroesRepository;
	}

	@Cacheable("superheroes")
	@Override
	public SuperHeroesResponse getAllSuperHeroes(Pageable pageable) {
		List<SuperHeroesDto> listOfSuperHeroes = new ArrayList<>();
		log.debug("buscando recursos en la bd");
		var response = superHeroesRepository.findAll(pageable);
		if (response.getContent().size() == 0) {
			log.debug("no existen recursos en la bd");
			throw new SuperHeroesNoContentException();
		}
		log.debug("se encontraron recursos en la bd");
		response.getContent().stream().forEach(
				superHeroe -> listOfSuperHeroes.add(new SuperHeroesDto(superHeroe.getId(), superHeroe.getName())));

		return new SuperHeroesResponse(listOfSuperHeroes, response.getTotalElements(), response.getTotalPages(),
				response.getNumber());
	}

	@Cacheable("superheroes")
	@Override
	public SuperHeroesDto getSuperHeroeById(String superHeroeId) {
		SuperHeroesDto result;
		var id = Long.parseLong(superHeroeId);
		log.debug("buscando recurso {} en la bd", superHeroeId);
		Optional<SuperHeroe> superHeroeDb = superHeroesRepository.findById(id);
		if (superHeroeDb.isPresent()) {
			log.debug("el recurso con id {} existe en la bd", superHeroeId);
			var superHeroe = superHeroeDb.get();
			result = new SuperHeroesDto(superHeroe.getId(), superHeroe.getName());
		} else {
			throw new SuperHeroesNotFoundException();
		}

		return result;
	}

	@Cacheable("superheroes")
	@Override
	public SuperHeroesResponse getSuperHeroesByName(String name, Pageable pageable) {
		List<SuperHeroesDto> listOfSuperHeroes = new ArrayList<>();
		log.debug("buscando recursos en la bd con los siguientes filtros name={}", name);
		var response = superHeroesRepository.findByNameContaining(name, pageable);
		if (response.getContent().size() == 0) {
			log.debug("no hay resultados para los filtros name={}", name);
			throw new SuperHeroesNoContentException();
		}
		log.debug("se han obtenido recursos con los siguientes filtros name={} en la bd", name);
		response.getContent().stream().forEach(
				superHeroe -> listOfSuperHeroes.add(new SuperHeroesDto(superHeroe.getId(), superHeroe.getName())));

		return new SuperHeroesResponse(listOfSuperHeroes, response.getTotalElements(), response.getTotalPages(),
				response.getNumber());
	}

	@Override
	public Long createSuperHeroe(SuperHeroesDto superHeroe) {

		var newSuperHeroe = new SuperHeroe();
		newSuperHeroe.setName(superHeroe.getName());
		log.debug("creando el recurso {} en la bd", superHeroe.toString());
		newSuperHeroe = superHeroesRepository.save(newSuperHeroe);

		return newSuperHeroe.getId();
	}

	@Override
	public SuperHeroesDto updateSuperHeroe(String idSuperHeroe, SuperHeroesDto newSuperHeroe) {

		var id = Long.parseLong(idSuperHeroe);
		log.debug("buscando el recurso {} en la bd", idSuperHeroe);
		var superHeroeDb = superHeroesRepository.findById(id);
		if (superHeroeDb.isPresent()) {
			log.debug("el recurso {} existe y se procede a actualizarlo", idSuperHeroe);
			var superHeroe = superHeroeDb.get();
			superHeroe.setName(newSuperHeroe.getName());

			superHeroe = superHeroesRepository.save(superHeroe);
			newSuperHeroe.setId(id);
		} else {
			log.debug("el recurso {} no existe", idSuperHeroe);
			throw new SuperHeroesNotFoundException();
		}
		return newSuperHeroe;
	}

	@Override
	public Boolean deleteSuperHeroe(String idSuperHeroe) {
		var id = Long.parseLong(idSuperHeroe);
		if (superHeroesRepository.existsById(id)) {
			log.debug("el recurso {} existe y se procede a eliminarlo", idSuperHeroe);
			superHeroesRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

}
