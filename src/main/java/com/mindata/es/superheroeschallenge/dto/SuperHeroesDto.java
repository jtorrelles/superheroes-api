package com.mindata.es.superheroeschallenge.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindata.es.superheroeschallenge.models.SuperHeroe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuperHeroesDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	@NotBlank(message = "el nombre del super heroe es obligatorio")
	private String name;

	public SuperHeroe toSuperHeroe() {
		return new SuperHeroe(id, name);
	}
}
