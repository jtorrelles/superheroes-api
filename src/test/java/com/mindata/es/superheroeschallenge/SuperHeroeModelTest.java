package com.mindata.es.superheroeschallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.models.SuperHeroe;

public class SuperHeroeModelTest {

	@Test
	public void toSuperHeroeDto_Test() {
		var superHeroe = new SuperHeroe(1L, "Superman");
		var superHeroeDto = superHeroe.toSuperHeroeDto();

		assertEquals(superHeroe.getName(), superHeroeDto.getName());
	}
	
	@Test
	public void toSuperHeroe_Test() {
		var superHeroeDto = new SuperHeroesDto(1L, "Superman");
		var superHeroe = superHeroeDto.toSuperHeroe();

		assertEquals(superHeroe.getName(), superHeroeDto.getName());
	}

}
