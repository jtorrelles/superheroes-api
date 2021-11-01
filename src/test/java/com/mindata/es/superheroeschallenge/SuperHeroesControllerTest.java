package com.mindata.es.superheroeschallenge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

		when(superHeroesService.getSuperHeroeById(anyString())).thenReturn(new SuperHeroesDto(1L, "Superman"));

		mockMvc.perform(get("/superheroes/{id}", "1")).andExpect(status().isOk());
	}

	@Test
	public void getSuperHeroeById_NotFound() throws Exception {
		when(superHeroesService.getSuperHeroeById(anyString())).thenThrow(new SuperHeroesNotFoundException());

		mockMvc.perform(get("/superheroes/{id}", "10")).andExpect(status().isNotFound());
	}

	@Test
	public void getSuperHeroeById_NotFoundwithAnyString() throws Exception {
		when(superHeroesService.getSuperHeroeById(anyString())).thenThrow(new NumberFormatException());

		mockMvc.perform(get("/superheroes/{id}", "0")).andExpect(status().isBadRequest());
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

		mockMvc.perform(get("/superheroes").param("name", anyString())).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("Superman", "Batman", "Wonder Woman")));
	}

	@Test
	public void getSuperHeroeByName_NotContent() throws Exception {
		when(superHeroesService.getSuperHeroesByName(anyString())).thenThrow(new SuperHeroesNoContentException());

		mockMvc.perform(get("/superheroes").param("name", "man")).andExpect(status().isNoContent());
	}

	@Test
	public void createSuperHeroe_Test() throws Exception {

		when(superHeroesService.createSuperHeroe(any(SuperHeroesDto.class))).thenReturn(4L);

		mockMvc.perform(post("/superheroes").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Catwoman\"}")).andExpect(status().isCreated())
				.andExpect(header().string("Location", "/superheroes/4"))
				.andExpect(jsonPath("$.name").value("Catwoman")).andExpect(jsonPath("$.id").value(4L));
	}

	@Test
	public void updateSuperHeroe_Test() throws Exception {

		when(superHeroesService.updateSuperHeroe(anyString(), any(SuperHeroesDto.class)))
				.thenReturn(new SuperHeroesDto(4L, "Cat Woman"));

		mockMvc.perform(put("/superheroes/{id}", "4").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content("{\"name\":\"Cat Woman\"}")).andExpect(status().isOk())
				.andExpect(header().string("Location", "/superheroes/4"))
				.andExpect(jsonPath("$.name").value("Cat Woman"));
	}

	@Test
	public void deleteSuperHeroe_Test() throws Exception {
		mockMvc.perform(delete("/superheroes/{id}", 4L)).andExpect(status().isNoContent());
	}

	@Test
	public void createSuperHeroeWithMissingName_shouldBeBadRequestResponse() throws Exception {
		mockMvc.perform(post("/superheroes").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content("{\"lastname\":\"Daredevil\"}")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[*]").value("el nombre del super heroe es obligatorio"));
	}
}
