package ars.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @Getter @Setter 
@ToString
public class AppointmentDTO {
	private Integer id;
	
	private Integer sessionId;
	private String clientEMail;
}
