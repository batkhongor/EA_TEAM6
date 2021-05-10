package ars.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Person;

public interface PersonService {

	public default List<Person> findAll() {
		throw new NotYetImplementedException();
	}

	public default Page<Person> findAllPaged(Pageable pageable) {
		throw new NotYetImplementedException();
	}

	public default Optional<Person> findById(Integer personId) {
		throw new NotYetImplementedException();
	}
	
	public default Person createPerson(Person person) {
		throw new NotYetImplementedException();
	}
	
	public default Person updatePerson(Person person) {
		throw new NotYetImplementedException();
	}
	
	public default void deletePerson(Integer personId) {
		throw new NotYetImplementedException();
	}
}
