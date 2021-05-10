package ars.service.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ars.domain.Person;
import ars.repository.PersonRepository;
import ars.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public List<Person> findAll() {
		return personRepository.findAll();
	}
	
	public Page<Person> findAllPaged(Pageable pageable) {
		return personRepository.findAll(pageable);
	}

	public Optional<Person> findById(Integer personId) {
		return personRepository.findById(personId);
	}
	
	public Person createPerson(Person person) {
		String encoded = passwordEncoder.encode(person.getPassword());
		person.setPassword(encoded);
		return personRepository.save(person);
	}
	
	public Person updatePerson(Person person) {
		String encoded = passwordEncoder.encode(person.getPassword());
		person.setPassword(encoded);
		return personRepository.save(person);
	}
	
	public void deletePerson(Integer personId) {
		personRepository.deleteById(personId);
	}
}
