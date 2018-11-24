package com.holkem.messenger.exceptions;

public class DataNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4884518180201883668L;

	public DataNotFoundException(String message) {
		super(message);
	}
}
