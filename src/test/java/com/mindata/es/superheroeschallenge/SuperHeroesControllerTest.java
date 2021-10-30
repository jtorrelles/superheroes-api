package com.mindata.es.superheroeschallenge;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mindata.es.superheroeschallenge.controllers.SuperHeroesController;
import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNoContentException;
import com.mindata.es.superheroeschallenge.exceptions.SuperHeroesNotFoundException;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SuperHeroesController.class)
public class SuperHeroesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SuperHeroesService superHeroesService;

	@Test
	public void getAllSuperHeroes_Test() throws Exception {

		when(superHeroesService.getAllSuperHeroes()).thenReturn(new ArrayList<SuperHeroesDto>() {
			{
				add(new SuperHeroesDto(1L, "Superman"));
				add(new SuperHeroesDto(2L, "Batman"));
			}
		});

		mockMvc.perform(MockMvcRequestBuilders.get("/superheroes")).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	public void getAllSuperHeroes_NotContent() throws Exception {
		when(superHeroesService.getAllSuperHeroes()).thenThrow(new SuperHeroesNoContentException());

		mockMvc.perform(MockMvcRequestBuilders.get("/superheroes")).andExpect(status().isNoContent());
	}

	@Test
	public void getSuperHeroeById_Test() throws Exception {

		when(superHeroesService.getSuperHeroeById(anyLong())).thenReturn(new SuperHeroesDto(1L, "Superman"));

		mockMvc.perform(get("/superheroes/{id}", anyLong())).andExpect(status().isOk());
	}

	@Test
	public void getSuperHeroeById_NotFound() throws Exception {
		when(superHeroesService.getSuperHeroeById(anyLong())).thenThrow(new SuperHeroesNotFoundException());

		mockMvc.perform(get("/superheroes/{id}", anyLong())).andExpect(status().isNotFound());
	}

	@Test
	public void getSuperHeroesByName_Test() throws Exception {

		when(superHeroesService.getSuperHeroesByName(anyString())).thenReturn(new ArrayList<SuperHeroesDto>() {
			{
				add(new SuperHeroesDto(1L, "Superman"));
				add(new SuperHeroesDto(2L, "Batman"));
				add(new SuperHeroesDto(3L, "Wonder Woman"));
			}
		});

		mockMvc.perform(get("/superheroes/find").param("name", anyString())).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("Superman", "Batman", "Wonder Woman")));
	}

	@Test
	public void getSuperHeroeByName_NotContent() throws Exception {
		when(superHeroesService.getSuperHeroesByName(anyString())).thenThrow(new SuperHeroesNoContentException());

		mockMvc.perform(get("/superheroes/find").param("name", "man")).andExpect(status().isNoContent());
	}
}
