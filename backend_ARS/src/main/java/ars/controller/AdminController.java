package ars.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ars.domain.Person;
import ars.dto.PersonDTO;
import ars.service.PersonService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PersonService personService;

	@GetMapping("/persons")
	public List<PersonDTO> getPersonList() {
		List<Person> list = personService.findAll();
		return list.stream().map(this::convertToPersonDto).collect(Collectors.toList());
	}

	@GetMapping(value = "/persons", params = "paged=true")
	public ResponseEntity<Page<PersonDTO>> getPersonList(Pageable pageable) {
		Page<Person> page = personService.findAllPaged(pageable);
		return ResponseEntity.ok(page.map(this::convertToPersonDto));
	}

	@GetMapping("/persons/{id}")
	public PersonDTO getPerson(@PathVariable("id") Integer personId) {
		Person entity = personService.findById(personId).get();
		return convertToPersonDto(entity);
	}

	@PostMapping("/persons")
	public PersonDTO createPerson(@Valid @RequestBody PersonDTO personDto) {
		Person entity = convertToEntity(personDto);
		entity = personService.createPerson(entity);
		return convertToPersonDto(entity);
	}

	@PutMapping("/persons/{id}")
	public PersonDTO updatePerson(@PathVariable("id") Integer personId, @Valid @RequestBody PersonDTO personDto) {
		personService.findById(personId).orElseThrow(RuntimeException::new);
		Person entity = convertToEntity(personDto);
		entity = personService.updatePerson(entity);
		return convertToPersonDto(entity);
	}

	@DeleteMapping("/persons/{id}")
	public void deletePerson(@PathVariable("id") Integer personId) {
		personService.deletePerson(personId);
	}

	private PersonDTO convertToPersonDto(Person person) {
		PersonDTO pDto = modelMapper.map(person, PersonDTO.class);
		pDto.setPassword("");
		return pDto;
	}

	private Person convertToEntity(PersonDTO pDto) {
		Person entity = modelMapper.map(pDto, Person.class);
		return entity;
	}
}
