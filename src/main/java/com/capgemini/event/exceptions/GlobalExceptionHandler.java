package com.capgemini.event.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String TIMESTAMP = "timestamp";
	private static final String MESSAGE = "message";
	private static final String STATUS = "status";

	@ExceptionHandler(QueryNotFoundException.class)
	public ResponseEntity<Object> handleQueryNotFound(QueryNotFoundException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<Object> handleInvalidPassword(InvalidPasswordException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RegistrationNotFoundException.class)
	public ResponseEntity<Object> handleRegistrationNotFound(RegistrationNotFoundException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EventNotFoundException.class)
	public ResponseEntity<Object> handleEventNotFound(EventNotFoundException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResponseNotFoundException.class)
	public ResponseEntity<Object> handleResponseNotFound(ResponseNotFoundException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FeedbackNotFoundException.class)
	public ResponseEntity<Object> handleFeedbackNotFound(FeedbackNotFoundException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TicketNotFoundException.class)
	public ResponseEntity<Object> handleTicketNotFound(TicketNotFoundException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, ex.getMessage());
		errorDetails.put(STATUS, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(STATUS, status.value());

		Map<String, String> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

		errorDetails.put("errors", fieldErrors);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptions(Exception ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put(TIMESTAMP, LocalDateTime.now());
		errorDetails.put(MESSAGE, "Unexpected error occurred");
		errorDetails.put("details", ex.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
