package com.translate.webtranslator.exception;

import java.time.LocalDateTime;

/**
 * The ResponseMessage class represents a custom response 
 * message in the Web-Text-Translator application.
 * It contains information about the time, status, message, and description of a response.
 */
public class ResponseMessage {

	private LocalDateTime time;
	private int status;
	private String message;
	private String description;
	
	/**
     * Default constructor for the ResponseMessage class.
     */
	public ResponseMessage() {	
	}
	
	 /**
     * Constructor for the ResponseMessage class.
     *
     * @param time        The LocalDateTime object representing the time of the response.
     * @param status      The status code of the response.
     * @param message     The message of the response.
     * @param description The description of the response.
     */
	public ResponseMessage(LocalDateTime time, int status, String message, String description) {
		this.time = time;
		this.status = status;
		this.message = message;
		this.description = description;
	}
	
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDateTime getTime() {
		return time;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDescription() {
		return description;
	}
}
