package com.mindata.es.superheroeschallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNoContentException;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNotFoundException;
import com.mindata.es.superheroeschallenge.models.SuperHeroe;
import com.mindata.es.superheroeschallenge.repository.SuperHeroesRepository;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;
import com.mindata.es.superheroeschallenge.services.impl.SuperHeroesServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SuperHeroesServiceTest {

	@Mock
	private SuperHeroesRepository superHeroesRepository;

	private SuperHeroesService superHeroeService;

	@BeforeEach
	public void setUp() throws Exception {
		superHeroeService = new SuperHeroesServiceImpl(superHeroesRepository);
	}

	@Test
	public void findAll_returnSuperHeroesInfo() {
		when(superHeroesRepository.findAll()).thenReturn(new ArrayList<SuperHeroe>() {
			{
				add(new SuperHeroe(1L, "Superman"));
				add(new SuperHeroe(2L, "Batman"));
			}
		});

		assertEquals(superHeroeService.getAllSuperHeroes().size(),2);
	}

	@Test
	public void findAll_returnNoContent() {
		when(superHeroesRepository.findAll()).thenReturn(new ArrayList<>());
		assertThrows(SuperHeroesNoContentException.class, () -> superHeroeService.getAllSuperHeroes());
	}

	@Test
	public void getSuperHeroeById_returnSuperHeroeInfo() {

		when(superHeroesRepository.findById(anyLong())).thenReturn(Optional.of(new SuperHeroe(1L, "Superman")));
		SuperHeroesDto shDto = superHeroeService.getSuperHeroeById(anyLong());
		assertEquals(shDto.getName(), "Superman");
	}

	@Test
	public void getSuperHeroeById_returnNotFound() {
		when(superHeroesRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(SuperHeroesNotFoundException.class, () -> superHeroeService.getSuperHeroeById(anyLong()));
	}

	@Test
	public void getSuperHeroeByName_returnSuperHeroeInfo() {

		when(superHeroesRepository.findByNameContaining(anyString())).thenReturn(new ArrayList<SuperHeroe>() {
			{
				add(new SuperHeroe(1L, "Superman"));
				add(new SuperHeroe(2L, "Batman"));
			}
		});

		List<SuperHeroesDto> shDto = superHeroeService.getSuperHeroesByName(anyString());
		assertEquals(shDto.size(), 2);
	}

	@Test
	public void getSuperHeroeByName_returnNoContent() {
		when(superHeroesRepository.findByNameContaining(anyString())).thenReturn(new ArrayList<>());
		assertThrows(SuperHeroesNoContentException.class, () -> superHeroeService.getSuperHeroesByName(anyString()));
	}
}
