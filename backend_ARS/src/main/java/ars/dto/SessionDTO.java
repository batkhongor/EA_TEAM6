package ars.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SessionDTO {
	private Integer id;

	@Future
	private LocalDate date;

	@NotNull
	private LocalTime startTime;

	@NotNull
	@Min(value = 1)
	private int duration; // in minutes

	@Length(max = 200)
	@NotNull
	private String location;
	
	@NotNull
	@Email
	private String providerEMail;
}
