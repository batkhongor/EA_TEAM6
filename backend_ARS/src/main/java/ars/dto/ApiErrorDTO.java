package ars.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @Getter @Setter 
@ToString
public class ApiErrorDTO {
	private LocalDateTime timestamp;
	private HttpStatus status;
	private String error;
	private String message;
	private String path;
	
	public ApiErrorDTO(HttpStatus status, String error, String message, String path) {
		super();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.timestamp = LocalDateTime.now();
	}
}


//{
//    "timestamp": "2021-05-13T12:42:02.268+00:00",
//    "status": 500,
//    "error": "Internal Server Error",
//    "message": "",
//    "path": "/client/sessions/1000/appointments"
//}