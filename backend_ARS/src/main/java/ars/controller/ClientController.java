package ars.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.Session;
import ars.service.ClientService;

@RestController
@RequestMapping("/client")
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

	@GetMapping("/appointments")
	private List<Appointment> findAppointments(Authentication authentication) {
		return clientService.findAllClientAppointments(authentication.getName());
	}
	
	@PostMapping("/sessions/{session_id}/appointments")
	private String createAppointment(
			Authentication authentication, 
			@PathVariable(name = "session_id") Integer sessionId) {
		
		return clientService.addNewAppointment(authentication.getName(), sessionId);
	}
	
	@PutMapping("/sessions/{session_id}/appointments/{appointment_id}")
	private String updateAppointment(
			Authentication authentication, 
			@PathVariable(name = "session_id") Integer session_id,
			@PathVariable(name = "appointment_id") Integer appointmentId) {
		
		return clientService.editAppointment(authentication.getName(), appointmentId, session_id);
	}
	
	
	@DeleteMapping("/appointments/{appointment_id}")
	private String updateAppointment(
			Authentication authentication,
			@PathVariable(name = "appointment_id") Integer appointmentId) {
		
		return clientService.deleteAppointment(authentication.getName(), appointmentId);
	}
}
