package ars.exceptions;

import java.util.function.Supplier;

public class NotFoundException extends Exception implements Supplier<NotFoundException> {

	private static final long serialVersionUID = 1L;
	
    public NotFoundException(String message) {
        super(message);
    }
    
	@Override
	public NotFoundException get() {
		return this;
	}
}
