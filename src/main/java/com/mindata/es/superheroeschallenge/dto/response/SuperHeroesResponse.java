package com.mindata.es.superheroeschallenge.dto.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuperHeroesResponse implements Serializable {

	private static final long serialVersionUID = -7027956828633968537L;
	private List<SuperHeroesDto> superheroes;
	private long totalItems;
	private long totalPage;
	private long currentPage;
}
