package ars.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ars.domain.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class PersonDTO {

	private Integer id;

	@NotNull
	@Length(max = 50)
	private String firstname;
	
	@NotNull
	@Length(max = 50)
	private String lastname;

	@NotNull
	@Email
	@Length(max = 100)
	private String email;
	
	@NotNull
	@Length(max = 100)
	private String password;
	
	private Set<RoleType> roles = new HashSet<>();
}
