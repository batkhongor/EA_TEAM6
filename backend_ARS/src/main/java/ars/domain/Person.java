package ars.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor

@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Column(length = 50, nullable = false)
	private String firstname;

	@NotNull
	@Column(length = 50, nullable = false)
	private String lastname;

	@NotNull
	@Email
	@Column(length = 100, nullable = false)
	private String email;

	@JsonIgnore
	@NotNull
	@Column(length = 128, nullable = false)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<RoleType> roles = new HashSet<>();

	public Person(@NotNull String firstname, @NotNull String lastname, @NotNull @Email String email,
			@NotNull String password, Set<RoleType> roles) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public void addRole(RoleType roleType) {
		roles.add(roleType);
	}

	public boolean hasRole(RoleType roleType) {
		return roles.contains(roleType);
	}

}
