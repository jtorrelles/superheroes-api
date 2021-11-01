package com.mindata.es.superheroeschallenge.controllers;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindata.es.superheroeschallenge.config.RequestExecutionTime;
import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.dto.response.SuperHeroesResponse;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/superheroes")
public class SuperHeroesController {

	private SuperHeroesService superHeroesService;

	public SuperHeroesController(SuperHeroesService superHeroesService) {
		this.superHeroesService = superHeroesService;
	}

	@Operation(summary = "Obtener un listado de super heroes")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Super heroes encontrados", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SuperHeroesDto.class)) }),
			@ApiResponse(responseCode = "204", description = "Super heroes no encontrados", content = @Content) })
	@RequestExecutionTime
	@GetMapping
	public ResponseEntity<SuperHeroesResponse> getAll(
			@RequestParam(required = false, name = "name") Optional<String> name,
			@RequestParam(defaultValue = "0") @Min(value = 0) int page,
			@RequestParam(defaultValue = "3") @Min(value = 1) int size) {
		Pageable paging = PageRequest.of(page, size);
		if (name.isPresent()) {
			log.debug("recibida peticion para buscar los super heroes segun la siguiente condicion name={}",
					name.get());
			return new ResponseEntity<SuperHeroesResponse>(superHeroesService.getSuperHeroesByName(name.get(), paging),
					HttpStatus.OK);
		}
		log.debug("recibida peticion para buscar los super heroes en la bd");
		return new ResponseEntity<SuperHeroesResponse>(superHeroesService.getAllSuperHeroes(paging), HttpStatus.OK);
	}

	@Operation(summary = "Obtener super heroe por id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Super heroe encontrado", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SuperHeroesDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Super heroe no encontrado", content = @Content) })
	@RequestExecutionTime
	@GetMapping("/{id}")
	public ResponseEntity<SuperHeroesDto> getById(@PathVariable("id") String id) {
		log.debug("recibida peticion para buscar super heroe segun el id {}", id);
		return new ResponseEntity<SuperHeroesDto>(superHeroesService.getSuperHeroeById(id), HttpStatus.OK);
	}

	@Operation(summary = "Crear un nuevo super heroe")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Super heroe creado", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SuperHeroesDto.class)) }) })
	@RequestExecutionTime
	@PostMapping
	public ResponseEntity<SuperHeroesDto> createSuperHeroe(@Valid @RequestBody SuperHeroesDto newSuperHeroe) {

		log.debug("recibida peticion para crear un nuevo super heroe {}", newSuperHeroe.toString());

		Long superHeroeId = superHeroesService.createSuperHeroe(newSuperHeroe);
		newSuperHeroe.setId(superHeroeId);

		String resourceLocation = new StringBuilder("/superheroes/").append(superHeroeId.toString()).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		headers.add("Location", resourceLocation);

		return new ResponseEntity<SuperHeroesDto>(newSuperHeroe, headers, HttpStatus.CREATED);
	}

	@Operation(summary = "Actualizar un super heroe existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Super heroe actualizado", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SuperHeroesDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Super heroe no encontrado", content = @Content) })
	@RequestExecutionTime
	@PutMapping("/{id}")
	public ResponseEntity<SuperHeroesDto> updateSuperHeroe(@PathVariable String id,
			@RequestBody(required = true) SuperHeroesDto newSuperHeroe) {

		log.debug("recibida peticion para actualizar super heroe con id {}, con los siguientes datos {}", id,
				newSuperHeroe.toString());
		superHeroesService.updateSuperHeroe(id, newSuperHeroe);

		String resourceLocation = new StringBuilder("/superheroes/").append(id).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		headers.add("Location", resourceLocation);

		return new ResponseEntity<SuperHeroesDto>(newSuperHeroe, headers, HttpStatus.OK);
	}

	@Operation(summary = "Eliminar un super heroe existente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Super heroe eliminado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Super heroe no encontrado", content = @Content) })
	@RequestExecutionTime
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> updateSuperHeroe(@PathVariable String id) {

		log.debug("recibida peticion para eliminar super heroe con id {}", id);
		superHeroesService.deleteSuperHeroe(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

}
