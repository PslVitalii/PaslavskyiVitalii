package com.epam.spring.homework3.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {
	private static final String MESSAGE = "%s with identifier: %s already exists";

	public EntityAlreadyExistsException(Object identifier, Class<?> type) {
		super(String.format(MESSAGE, type.getSimpleName(), identifier));
	}
}
