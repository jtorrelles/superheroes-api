package com.mindata.es.superheroeschallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

		assertEquals(superHeroeService.getAllSuperHeroes().size(), 2);
	}

	@Test
	public void findAll_returnNoContent() {
		when(superHeroesRepository.findAll()).thenReturn(new ArrayList<>());
		assertThrows(SuperHeroesNoContentException.class, () -> superHeroeService.getAllSuperHeroes());
	}

	@Test
	public void getSuperHeroeById_returnSuperHeroeInfo() {

		when(superHeroesRepository.findById(anyLong())).thenReturn(Optional.of(new SuperHeroe(1L, "Superman")));
		SuperHeroesDto shDto = superHeroeService.getSuperHeroeById("1");
		assertEquals(shDto.getName(), "Superman");
	}

	@Test
	public void getSuperHeroeById_returnNotFound() {
		when(superHeroesRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(SuperHeroesNotFoundException.class, () -> superHeroeService.getSuperHeroeById("1"));
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

	@Test
	public void createSuperHeroe_returnCreatedSuperHeroe() {
		var newSuperHeroeMock = new SuperHeroesDto();
		newSuperHeroeMock.setName("Green Latern");

		when(superHeroesRepository.save(any(SuperHeroe.class))).thenReturn(new SuperHeroe(1L, "Green Latern"));

		assertNotNull(superHeroeService.createSuperHeroe(newSuperHeroeMock));
	}

	@Test
	public void updateSuperHeroe_returnUpdatedSuperHeroe() {

		SuperHeroesDto input = new SuperHeroesDto();
		input.setName("Green Latern");

		when(superHeroesRepository.findById(anyLong())).thenReturn(Optional.of(new SuperHeroe(4L, "Green Latern")));
		when(superHeroesRepository.save(any(SuperHeroe.class))).thenReturn(any(SuperHeroe.class));

		var updatedSuperHeroe = superHeroeService.updateSuperHeroe("1", input);
		assertNotNull(updatedSuperHeroe);
	}

	@Test
	public void updateSuperHeroe_returnNotFound() {
		when(superHeroesRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(SuperHeroesNotFoundException.class,
				() -> superHeroeService.updateSuperHeroe("1", new SuperHeroesDto(1L, "Superman")));
	}

	@Test
	public void deleteSuperHeroe_shouldBeNoContent() {
		when(superHeroesRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
		assertTrue(superHeroeService.deleteSuperHeroe("1"));
	}

	@Test
	public void deleteSuperHeroe_shouldBeNotFound() {
		when(superHeroesRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);
		assertFalse(superHeroeService.deleteSuperHeroe("1"));
	}
}
