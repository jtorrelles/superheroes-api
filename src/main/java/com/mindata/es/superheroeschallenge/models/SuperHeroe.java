package com.mindata.es.superheroeschallenge.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "super_heroes")
public class SuperHeroe {
	@Id
	private long id;
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String name;
}
