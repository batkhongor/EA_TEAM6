package ars.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String firstname;
	private String lastname;

	@ManyToMany
	@JoinTable(name = "person_role", 
			joinColumns = {@JoinColumn(name = "person__id") }, 
			inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	public Person(String firstname, String lastname, Set<Role> roles) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.roles = roles;
	}
	
	
}
