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
	private ClientService clientService;
	
	//get all providers
    @GetMapping
    public List<Person> findAllClients() {
        return clientService.findAllClients();
    }
    
    // ------------------- SESSION CRUD ----------------------
	@GetMapping("/sessions")
	private List<Session> findAllSessions() {
		return clientService.findAllSessions();
	}

	// ------------------- APPOINTMENTS CRUD ----------------------
	@GetMapping("/appointments")
	private List<Appointment> findAppointments(Authentication authentication) {
		return clientService.findAllClientAppointments(authentication.getName());
	}
	
	@PostMapping("/sessions/{session_id}/appointments")
	private void createAppointment(
			Authentication authentication, 
			@PathVariable(name = "session_id") Integer sessionId) throws IllegalAccessException {
		
		clientService.addNewAppointment(authentication.getName(), sessionId);
	}
}
