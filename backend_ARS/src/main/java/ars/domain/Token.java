package ars.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Token {
	
	@Id
	@EqualsAndHashCode.Include
	private String jwt;
	
	private boolean valid;
	
	@ManyToOne
	@JoinColumn(name="person_id")
	private Person owner;
	
	private Token() {
		// token cannot be initialized so. It should be private
	}
	
	public Token(String jwt, Person owner) {
		this.jwt=jwt;
		this.owner=owner;
		this.valid=true;
	}
	
	public void setOwner(Person owner) {
		this.owner=owner;
	}
	
	public void setValid() {
		// once token status is changed, we should not change status again
		this.valid=false;
	}
}
