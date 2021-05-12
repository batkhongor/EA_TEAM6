package ars.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Person;

public interface PersonService {

	List<Person> findAll();

	Page<Person> findAll(Pageable pageable);

	Optional<Person> findById(Integer personId);
	
	Person createPerson(Person person);
	
	Person updatePerson(Person person);
	
	void deletePerson(Integer personId);
}
