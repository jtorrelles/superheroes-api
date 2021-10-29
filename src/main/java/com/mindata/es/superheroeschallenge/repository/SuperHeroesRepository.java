package com.mindata.es.superheroeschallenge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mindata.es.superheroeschallenge.models.SuperHeroe;

@Repository
public interface SuperHeroesRepository extends CrudRepository<SuperHeroe, Long>{

}
