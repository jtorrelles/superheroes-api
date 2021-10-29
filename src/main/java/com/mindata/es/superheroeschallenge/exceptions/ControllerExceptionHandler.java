package com.mindata.es.superheroeschallenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(value = { SuperHeroesNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String superHeroesNotFound(SuperHeroesNotFoundException exception) {
		return null;
	}

	@ExceptionHandler(value = { SuperHeroesNoContentException.class })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String superHeroesNoContent(SuperHeroesNoContentException exception) {
		return null;
	}
}
