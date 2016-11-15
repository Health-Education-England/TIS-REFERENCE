package com.transformuk.hee.tis.reference.exception;

import com.transformuk.hee.tis.reference.model.ErrorMessageKey;
import com.transformuk.hee.tis.reference.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.transformuk.hee.tis.reference.model.ErrorType.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ExceptionHandlerController {

	/**
	 * Handles an exception and returns {@link ResponseEntity} with errorMessage
	 * @param ex Exception to be handled
	 * @return {@link ResponseEntity}
	 * @throws IOException
	 */
	@ExceptionHandler(value = {Exception.class, RuntimeException.class})
	public ResponseEntity<Map<String,Object>> handleException(Exception ex) throws IOException {
		HttpStatus status = INTERNAL_SERVER_ERROR;
		Map<String,Object> errorMap = new HashMap<>();
		ex.printStackTrace();
		errorMap.put("errorMessage", ex.getMessage());
		return new ResponseEntity<>(errorMap, status);
	}

	/**
	 * Handles an MethodArgumentNotValidException and returns {@link ResponseEntity} with errorMessage
	 *
	 * @param ex Exception to be handled
	 * @return {@link ResponseEntity}
	 */
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	@ResponseStatus(BAD_REQUEST)
	@ResponseBody
	public List<ErrorResponse> processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		return processErrors(result);
	}

	private List<ErrorResponse> processErrors(BindingResult result) {
		List<ErrorResponse> errorResponses = newArrayList();
		for (org.springframework.validation.ObjectError objectError : result.getAllErrors()) {
			ErrorResponse errorResponse;
			if (objectError instanceof FieldError) {
				FieldError fieldError = (FieldError) objectError;
				errorResponse = new ErrorResponse(VALIDATION_ERROR, null, fieldError.getField(), getMessageKey(objectError.getDefaultMessage()), fieldError.getRejectedValue());
			} else {
				errorResponse = new ErrorResponse(VALIDATION_ERROR, getMessageKey(objectError.getDefaultMessage()));
			}
			errorResponses.add(errorResponse);

		}
		return errorResponses;
	}

	private ErrorMessageKey getMessageKey(String value) {
		for(ErrorMessageKey key : ErrorMessageKey.values()) {
			if(key.getValue().equals(value))
				return key;
		}
		return null;
	}
}
