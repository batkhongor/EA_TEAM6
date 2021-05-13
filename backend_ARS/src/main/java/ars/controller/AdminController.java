package ars.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.Session;
import ars.dto.AppointmentDTO;
import ars.dto.PersonDTO;
import ars.dto.SessionDTO;
import ars.exceptions.NotAllowedException;
import ars.exceptions.NotFoundException;
import ars.exceptions.TimeConflictException;
import ars.service.AdminService;
import ars.service.AppointmentService;
import ars.service.PersonService;
import ars.service.SessionService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PersonService personService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private AdminService adminService;

	/* <PERSON> */
	@GetMapping(value = "/persons")
	public Page<PersonDTO> getPersonList(Pageable pageable) {
		Page<Person> page = personService.findAll(pageable);
		return page.map(this::convertToPersonDto);
	}

	@PostMapping("/persons")
	public PersonDTO createPerson(@Valid @RequestBody PersonDTO personDto) {
		Person entity = convertToEntity(personDto);
		entity = personService.createPerson(entity);
		return convertToPersonDto(entity);
	}

	@PutMapping("/persons/{id}")
	public PersonDTO updatePerson(@PathVariable("id") Integer personId, @Valid @RequestBody PersonDTO personDto) {
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
		Page<Session> page = sessionService.findAll(pageable, false);
		return page;
	}

	@GetMapping(value = "/sessions", params = "futureOnly=true")
	public Page<Session> getFutureSessionList(Pageable pageable) {
		Page<Session> page = sessionService.findAll(pageable, true);
		return page;
	}

	@GetMapping("/sessions/{id}")
	public Session getSession(@PathVariable("id") Integer sessionId) throws NotFoundException {
		Session entity = sessionService.getSession(sessionId);
		return entity;
	}

	//------------GET SESSIONS FOR A PARTICULAR SESSION--------PLEASE DONT REMOVE
	//Get Sessions for a certain provider  ..http://localhost:8009/admin/sessions/provider?providerId={id}
	@GetMapping("/sessions/provider")
	public List<Session> getSessionforProvider(@RequestParam(name = "providerId")  Integer providerId, Authentication authentication) throws NotFoundException, NotAllowedException {
		List<Session> sessions = sessionService.findSessionForProvider(providerId, authentication.getName());
		return sessions;
	}

	//--------------------------------------------------------------------------------------

	@PostMapping("/sessions")
	public Session createSession(@Valid @RequestBody SessionDTO sessionDto)
			throws TimeConflictException, NotAllowedException {
		Session entity = convertToEntity(sessionDto);
		entity = sessionService.createSession(entity, sessionDto.getProviderEMail());
		return entity;
	}

	@PutMapping("/sessions/{id}")
	public Session updateSession(@PathVariable("id") Integer sessionId, @Valid @RequestBody SessionDTO sessionDto)
			throws TimeConflictException, NotAllowedException, NotFoundException {
		Session entity = convertToEntity(sessionDto);
		entity = sessionService.updateSession(sessionId, entity, sessionDto.getProviderEMail());
		return entity;
	}

	@DeleteMapping("/sessions/{id}")
	public void deleteSession(@PathVariable("id") Integer sessionId) {
		sessionService.deleteSession(sessionId);
	}

	/* </SESSION> */

	/* <APPOINTMENT> */

	@GetMapping(value = "/appointments")
	public Page<Appointment> getAppointmentList(Pageable pageable) {
		Page<Appointment> page = appointmentService.findAllAppointments(pageable);
		return page;
	}

//	@GetMapping("/appointments/{id}")
//	public Appointment getAppointment(@PathVariable("id") Integer appointmentId) throws NotFoundException {
//		Appointment entity = appointmentService.getAppointment(appointmentId);
//		return entity;
//	}

	@PostMapping("/appointments")
	public Appointment createAppointment(@Valid @RequestBody AppointmentDTO appointmentDto)
			throws TimeConflictException, NotAllowedException, NotFoundException {
		Appointment entity = appointmentService.createAppointment(appointmentDto.getClientEMail(),
				appointmentDto.getSessionId());
		return entity;
	}

	@PutMapping("/appointments/{id}")
	public Appointment updateAppointment(Authentication authentication, @PathVariable("id") Integer appointmentId,
			@Valid @RequestBody AppointmentDTO appointmentDto)
			throws TimeConflictException, NotAllowedException, NotFoundException {

		Appointment entity = appointmentService.editAppointment(authentication.getName(), appointmentId,
				appointmentDto.getSessionId());
		return entity;
	}

	@DeleteMapping("/appointments/{id}")
	public Appointment deleteAppointment(@PathVariable("id") Integer appointmentId, Authentication authentication)
			throws NotFoundException, NotAllowedException, TimeConflictException {
		return appointmentService.deleteAppointment(authentication.getName(), appointmentId);
	}

	/* </APPOINTMENT> */

	/* <private methods> */
	private PersonDTO convertToPersonDto(Person person) {
		PersonDTO pDto = modelMapper.map(person, PersonDTO.class);
		pDto.setPassword(null);
		return pDto;
	}

	private Person convertToEntity(PersonDTO pDto) {
		Person entity = modelMapper.map(pDto, Person.class);
		return entity;
	}

	private Session convertToEntity(SessionDTO pDto) {
		Session entity = modelMapper.map(pDto, Session.class);
		return entity;
	}

	/* </private methods> */
}
