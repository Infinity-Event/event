package com.capgemini.event.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QueryNotFoundException extends RuntimeException {
	public QueryNotFoundException(String message) {
		super(message);
	}

}