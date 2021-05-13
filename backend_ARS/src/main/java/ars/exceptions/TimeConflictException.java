package ars.exceptions;

public class TimeConflictException extends Exception {

	private static final long serialVersionUID = 1L;

	public TimeConflictException(String message) {
		super(message);
	}
}
