package com.mindata.es.superheroeschallenge.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, List<String>> bodyResponse = new HashMap<>();

		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

		bodyResponse.put("errors", errors);
		return bodyResponse;
	}

	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleNumberFormatException(NumberFormatException ex) {
		return null;
	}
}
