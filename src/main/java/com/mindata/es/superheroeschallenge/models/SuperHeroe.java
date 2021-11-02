package com.mindata.es.superheroeschallenge.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "super_heroes")
public class SuperHeroe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "el nombre del super heroe es obligatorio")
	private String name;
	
	public SuperHeroesDto toSuperHeroeDto() {
		return new SuperHeroesDto(id, name);
	}
}
