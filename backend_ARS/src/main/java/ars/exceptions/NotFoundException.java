package ars.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception implements Supplier<NotFoundException> {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(message);
	}

	@Override
	public NotFoundException get() {
		return new NotFoundException("Not found");
	}
}
