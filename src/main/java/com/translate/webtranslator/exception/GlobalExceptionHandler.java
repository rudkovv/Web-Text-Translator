package com.translate.webtranslator.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * The GlobalExceptionHandler class is responsible for handling
 * global exceptions in the Web-Text-Translator application.
 * It provides exception handling methods for various types of 
 * exceptions and returns a customized response message.
 */
@RestControllerAdvice(annotations = RestExceptionHandler.class)
public class GlobalExceptionHandler {
     /**
     * Handles the NoHandlerFoundException and returns a customized 
     * response message with a 404 status code.
     *
     * @param request The HttpServletRequest object.
     * @param ex The NoHandlerFoundException object.
     * @return A ResponseEntity containing the customized response message.
     */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ResponseMessage> handle404Error(
		   HttpServletRequest request, NoHandlerFoundException ex) {
	    ResponseMessage responseMessage = new ResponseMessage();
	    responseMessage.setTime(LocalDateTime.now());
	    responseMessage.setStatus(HttpStatus.NOT_FOUND.value());
	    responseMessage.setMessage("Not found " + request.getRequestURI());
		responseMessage.setDescription("Try to entry right url");
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
	}
	
	  /**
     * Handles the EntityNotFoundException and returns a customized response 
     * message with a 400 status code.
     *
     * @param ex The EntityNotFoundException object.
     * @return A ResponseEntity containing the customized response message.
     */
	@ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> badRequestException(EntityNotFoundException ex) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setTime(LocalDateTime.now());
		responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
		responseMessage.setMessage(ex.getLocalizedMessage());
		responseMessage.setDescription("The requested entity was not found");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }
	
  /**
    * Handles the MethodArgumentTypeMismatchException and returns a customized
    * response message with a 400 status code.
    *
    * @param ex The MethodArgumentTypeMismatchException object.
    * @return A ResponseEntity containing the customized response message.
    */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> badRequestException(
    		                               MethodArgumentTypeMismatchException ex) {
    	ResponseMessage responseMessage = new ResponseMessage();
    	responseMessage.setTime(LocalDateTime.now());
		responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
    	responseMessage.setMessage(ex.getLocalizedMessage());
    	responseMessage.setDescription("The type of the argument does not match the expected type");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }

    /**
    * Handles the HttpMessageNotReadableException and returns a customized response
    * message with a 400 status code.
    *
    * @param ex The HttpMessageNotReadableException object.
    * @return A ResponseEntity containing the customized response message.
    */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseMessage> badRequestException(
		   HttpMessageNotReadableException ex) {
	    ResponseMessage responseMessage = new ResponseMessage();
	    responseMessage.setTime(LocalDateTime.now());
		responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
	    responseMessage.setMessage(ex.getLocalizedMessage());
		responseMessage.setDescription("The HTTP message body cannot be read or parsed");
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
	}

    /**
     * Handles the MissingServletRequestParameterException and returns a customized response
     * message with a 400 status code.
     *
     * @param ex The MissingServletRequestParameterException object.
     * @return A ResponseEntity containing the customized response message.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> handleMissingServletRequestParameterException(
    		                               MissingServletRequestParameterException ex) {
    	ResponseMessage responseMessage = new ResponseMessage();
    	responseMessage.setTime(LocalDateTime.now());
		responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
    	responseMessage.setMessage(ex.getLocalizedMessage());
    	responseMessage.setDescription("One or more required request parameters are missing");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }

    /**
     * Handles the MissingPathVariableException and returns a customized response
     * message with a 400 status code.
     *
     * @param ex The MissingPathVariableException object.
     * @return A ResponseEntity containing the customized response message.
     */
    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> handleMissingPathVariableException(
    		                               MissingPathVariableException ex) {
    	ResponseMessage responseMessage = new ResponseMessage();
    	responseMessage.setTime(LocalDateTime.now());
		responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
    	responseMessage.setMessage(ex.getLocalizedMessage());
    	responseMessage.setDescription("One or more required path variables are missing");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }
    
    /**
     * Handles the HttpClientErrorException and returns a customized
     * response message with a 400 status code.
     *
     * @param ex The HttpClientErrorException object.
     * @return A ResponseEntity containing the customized response message.
     */
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> handleHttpClientErrorException(
    		                               HttpClientErrorException ex) {
    	ResponseMessage responseMessage = new ResponseMessage();
    	responseMessage.setTime(LocalDateTime.now());
		responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
    	responseMessage.setMessage(ex.getLocalizedMessage());
    	responseMessage.setDescription("Invalid request to the server, analyze the logs");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }
    
    /**
     * Handles the HttpServerErrorException and returns a customized response
     * message with a 500 status code.
     *
     * @param ex The HttpServerErrorException object.
     * @return A ResponseEntity containing the customized response message.
     */
    @ExceptionHandler(HttpServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseMessage> handleHttpServerErrorException(
    		                               HttpServerErrorException ex) {
    	ResponseMessage responseMessage = new ResponseMessage();
    	responseMessage.setTime(LocalDateTime.now());
		responseMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	responseMessage.setMessage(ex.getLocalizedMessage());
    	responseMessage.setDescription("The server encountered an error when processing the request, analyze the logs to determine the exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }
    
    /**
     * Handles the Exception class and returns a customized response message
     * with a 500 status code.
     *
     * @param ex The Exception object.
     * @return A ResponseEntity containing the customized response message.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseMessage> handleValidationExceptions(Exception ex) {
    	ResponseMessage responseMessage = new ResponseMessage();
    	responseMessage.setTime(LocalDateTime.now());
		responseMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	responseMessage.setMessage(ex.getLocalizedMessage());
    	responseMessage.setDescription("Analyze the logs to determine the exception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
    }
    
}