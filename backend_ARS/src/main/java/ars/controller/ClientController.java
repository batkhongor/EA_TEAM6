package ars.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.dto.PersonDTO;
import ars.repository.AppointmentRepository;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import ars.service.ClientService;

@RestController
@RequestMapping("/clients") // plural matters?
public class ClientController {
	
	@Autowired
    private PersonRepository personRepository;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private ClientService clientService;
	
	//get all providers
    @GetMapping
    public List<Person> findAllClients() {
        return personRepository.findAll().stream().filter(person -> person.getRoles()
                                          .contains(RoleType.CUSTOMER)).collect(Collectors.toList());
    }
    
    // ------------------- SESSION CRUD ----------------------
	@GetMapping("/sessions")
	private List<Session> findAllSessions() {
		return sessionRepository.findAll();
	}

	// ------------------- APPOINTMENTS CRUD ----------------------
	@GetMapping("/appointments")
	private List<Appointment> findAppointments(Authentication authentication) {
		System.out.println("EMAIL:" + authentication.getName());
		return appointmentRepository.findByClientEmail(authentication.getName());
	}
	
	@PostMapping("/appointments")
	private void createAppointment(Authentication authentication, @RequestBody Appointment appointment) {
		System.out.println("POST /appointments");
		System.out.println("EMAIL:" + authentication.getName());
		
		String email = authentication.getName();
		Person client = personRepository.findByEmail(email);
		Session session = sessionRepository.findById(sessionId);
		
		Appointment appointment = new Appointment(..., client, session);
		personRep
		System.out.println(appointment);
		
		return clientService.addNewAppointment(email, sessionId);
	}
}
