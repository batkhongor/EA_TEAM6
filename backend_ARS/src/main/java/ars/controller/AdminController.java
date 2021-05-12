package ars.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ars.domain.Person;
import ars.domain.Session;
import ars.dto.PersonDTO;
import ars.service.AdminService;
import ars.service.PersonService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PersonService personService;

	@Autowired
	private AdminService adminService;

	/* <PERSON> */
	@GetMapping(value = "/persons")
	public Page<PersonDTO> getPersonList(Pageable pageable) {
		Page<Person> page = personService.findAllPaged(pageable);
		return page.map(this::convertToPersonDto);
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
	/* </PERSON> */

	/* <SESSION> */

	@GetMapping(value = "/sessions")
	public Page<Session> getSessionList(Pageable pageable) {
		Page<Session> page = adminService.findAllSessions(pageable);
		return page;
	}

	@GetMapping("/sessions/{id}")
	public Session getSession(@PathVariable("id") Integer personId) {
		Session entity = adminService.findSessionById(personId).get();
		return entity;
	}

	@PostMapping("/sessions")
	public Session createSession(@Valid @RequestBody Session personDto) {
		Session entity = personDto;
		entity = adminService.createSession(entity);
		return entity;
	}

	@PutMapping("/sessions/{id}")
	public Session updateSession(@PathVariable("id") Integer sessionId, @Valid @RequestBody Session sessionDto) {
		adminService.findSessionById(sessionId).orElseThrow(RuntimeException::new);
		Session entity = sessionDto;
		entity = adminService.updateSession(entity);
		return entity;
	}

	@DeleteMapping("/sessions/{id}")
	public void deleteSession(@PathVariable("id") Integer personId) {
		personService.deletePerson(personId);
	}

	/* </SESSION> */

	/* <private methods> */
	private PersonDTO convertToPersonDto(Person person) {
		PersonDTO pDto = modelMapper.map(person, PersonDTO.class);
		pDto.setPassword("");
		return pDto;
	}

	private Person convertToEntity(PersonDTO pDto) {
		Person entity = modelMapper.map(pDto, Person.class);
		return entity;
	}
	/* </private methods> */
}
