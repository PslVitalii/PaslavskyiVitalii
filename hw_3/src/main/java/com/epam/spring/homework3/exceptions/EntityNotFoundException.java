package com.epam.spring.homework3.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
	private static final String MESSAGE = "%s with identifier: %s not found";

	public EntityNotFoundException(Object identifier, Class<?> entity) {
		super(String.format(MESSAGE, entity.getSimpleName(), identifier));
	}
}