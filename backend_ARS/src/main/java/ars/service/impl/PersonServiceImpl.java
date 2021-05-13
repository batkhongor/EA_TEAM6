package ars.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Person;
import ars.repository.PersonRepository;
import ars.service.PersonService;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Person> findAll() {
		return personRepository.findAll();
	}

	public Page<Person> findAll(Pageable pageable) {
		return personRepository.findAll(pageable);
	}

	public Optional<Person> findById(Integer personId) {
		return personRepository.findById(personId);
	}

	public Optional<Person> findByEmail(String email) {
		return Optional.of(personRepository.findByEmailOne(email));
	}

	public Person createPerson(Person person) {
		String encoded = passwordEncoder.encode(person.getPassword());
		person.setPassword(encoded);
		return personRepository.save(person);
	}

	public Person updatePerson(Person person) {
		Person entity = personRepository.findById(person.getId()).get();

		entity.setFirstname(person.getFirstname());
		entity.setFirstname(person.getLastname());
		entity.setEmail(person.getEmail());
		entity.setRoles(person.getRoles());

		if (person.getPassword() != null && person.getPassword().isEmpty()) {
			String encoded = passwordEncoder.encode(person.getPassword());
			entity.setPassword(encoded);
		}

		return personRepository.save(entity);
	}

	public void deletePerson(Integer personId) {
		personRepository.deleteById(personId);
	}
}
