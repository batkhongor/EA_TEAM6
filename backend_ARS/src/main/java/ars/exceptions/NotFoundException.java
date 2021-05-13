package ars.exceptions;

import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Hello from the other side")
public class NotFoundException extends RuntimeException implements Supplier<NotFoundException> {

	private static final long serialVersionUID = 1L;
	
	public NotFoundException() {
        super();
        System.out.println("-------- here1 -----------");    }
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("-------- here2 -----------");
    }
    public NotFoundException(String message) {
        super(message);
        System.out.println("-------- here3 -----------");
    }
    public NotFoundException(Throwable cause) {
        super(cause);
        System.out.println("-------- here4 -----------");
    }
    
	@Override
	public NotFoundException get() {
		System.out.println("-------- supplier get() -----------");
		return this;
	}
}
