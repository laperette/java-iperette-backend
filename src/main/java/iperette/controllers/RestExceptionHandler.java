package iperette.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import iperette.exceptions.ApiError;
import iperette.exceptions.DatesAlreadyBookedExceptions;
import iperette.exceptions.NoGuestNumberGivenException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DatesAlreadyBookedExceptions.class)
	public ResponseEntity<Object> handleBadBookingDates(DatesAlreadyBookedExceptions ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "already booked", ex);
		return buildResponseEntity(apiError);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoGuestNumberGivenException.class)
	public ResponseEntity<Object> handleNoGuestNumber(NoGuestNumberGivenException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "no guest number provided", ex);
		return buildResponseEntity(apiError);
	}

}