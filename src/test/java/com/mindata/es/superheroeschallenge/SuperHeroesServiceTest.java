package com.mindata.es.superheroeschallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
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
		Iterable<SuperHeroe> shMockedList = new ArrayList<SuperHeroe>() {
			{
				add(new SuperHeroe(1L, "Superman"));
				add(new SuperHeroe(2L, "Batman"));
			}
		};
		when(superHeroesRepository.findAll()).thenReturn(shMockedList);

		List<SuperHeroesDto> result = new ArrayList<>();
		shMockedList.forEach(superHeroe -> result.add(new SuperHeroesDto(superHeroe.getId(), superHeroe.getName())));

		assertEquals(superHeroeService.getAllSuperHeroes().size(), result.size());
	}

	@Test
	public void findAll_returnNotFound() {
		when(superHeroesRepository.findAll()).thenReturn(new ArrayList<>());
		assertThrows(SuperHeroesNotFoundException.class, () -> superHeroeService.getAllSuperHeroes());
	}
}
