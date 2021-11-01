package com.mindata.es.superheroeschallenge.controllers;

import java.util.List;
import java.util.Optional;

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

import com.mindata.es.superheroeschallenge.dto.SuperHeroesDto;
import com.mindata.es.superheroeschallenge.services.SuperHeroesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
	@GetMapping
	public ResponseEntity<List<SuperHeroesDto>> getAll(
			@RequestParam(required = false, name = "name") Optional<String> name) {
		if (name.isPresent()) {
			return new ResponseEntity<List<SuperHeroesDto>>(superHeroesService.getSuperHeroesByName(name.get()),
					HttpStatus.OK);
		}
		return new ResponseEntity<List<SuperHeroesDto>>(superHeroesService.getAllSuperHeroes(), HttpStatus.OK);
	}

	@Operation(summary = "Obtener super heroe por id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Super heroe encontrado", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SuperHeroesDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Super heroe no encontrado", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<SuperHeroesDto> getById(@PathVariable long id) {
		return new ResponseEntity<SuperHeroesDto>(superHeroesService.getSuperHeroeById(id), HttpStatus.OK);
	}

	@Operation(summary = "Crear un nuevo super heroe")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Super heroe creado", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SuperHeroesDto.class)) }) })
	@PostMapping
	public ResponseEntity<SuperHeroesDto> createSuperHeroe(@RequestBody SuperHeroesDto newSuperHeroe) {

		Long superHeroeId = superHeroesService.createSuperHeroe(newSuperHeroe);
		newSuperHeroe.setId(superHeroeId);

		String resourceLocation = new StringBuilder("/superheroes/").append(superHeroeId.toString()).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		headers.add("Location", resourceLocation);

		return new ResponseEntity<SuperHeroesDto>(newSuperHeroe, headers, HttpStatus.OK);
	}

	@Operation(summary = "Actualizar un super heroe existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Super heroe actualizado", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SuperHeroesDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Super heroe no encontrado", content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<SuperHeroesDto> updateSuperHeroe(@PathVariable Long id,
			@RequestBody(required = true) SuperHeroesDto newSuperHeroe) {

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
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> updateSuperHeroe(@PathVariable Long id) {
		superHeroesService.deleteSuperHeroe(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

}
