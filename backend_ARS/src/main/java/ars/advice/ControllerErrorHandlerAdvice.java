package ars.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ars.dto.ApiErrorDTO;
import ars.exceptions.NotAllowedException;
import ars.exceptions.NotFoundException;
import ars.exceptions.TimeConflictException;

@ControllerAdvice
public class ControllerErrorHandlerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { NotFoundException.class })
	protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
		ApiErrorDTO bodyOfResponse = new ApiErrorDTO(HttpStatus.NOT_FOUND, "NOT FOUND", ex.getLocalizedMessage(),
				request.getDescription(false));

		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = { NotAllowedException.class, TimeConflictException.class })
	protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
		ApiErrorDTO bodyOfResponse = new ApiErrorDTO(HttpStatus.BAD_REQUEST, "BAD REQUEST", ex.getLocalizedMessage(),
				request.getDescription(false));

		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}
