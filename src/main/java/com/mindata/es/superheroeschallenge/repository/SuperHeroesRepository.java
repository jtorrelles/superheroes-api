package com.mindata.es.superheroeschallenge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mindata.es.superheroeschallenge.models.SuperHeroe;

@Repository
public interface SuperHeroesRepository extends PagingAndSortingRepository<SuperHeroe, Long> {
	Page<SuperHeroe> findByNameContaining(String name, Pageable pageable);

	Page<SuperHeroe> findAll(Pageable pageable);
}
