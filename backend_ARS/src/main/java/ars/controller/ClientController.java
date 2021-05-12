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
import ars.domain.Session;
import ars.exceptions.NotAllowedException;
import ars.exceptions.NotFoundException;
import ars.exceptions.TimeConflictException;
import ars.service.AppointmentService;
import ars.service.SessionService;

@RestController
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private SessionService sessionService;
	
    
    // ------------------- SESSION CRUD ----------------------
	@GetMapping("/sessions")
	private List<Session> findFutureSessions() {
		return sessionService.findAll(true);
	}

	@GetMapping("/appointments")
	private List<Appointment> findAppointments(Authentication authentication) {
		return appointmentService.findAllClientAppointments(authentication.getName());
	}
	
	@PostMapping("/sessions/{session_id}/appointments")
	private Appointment createAppointment(
			Authentication authentication, 
			@PathVariable(name = "session_id") Integer sessionId) throws NotFoundException {
		return appointmentService.createAppointment(authentication.getName(), sessionId);
	}
	
	@PutMapping("/sessions/{session_id}/appointments/{appointment_id}")
	private Appointment updateAppointment(
			Authentication authentication, 
			@PathVariable(name = "session_id") Integer session_id,
			@PathVariable(name = "appointment_id") Integer appointmentId) throws NotFoundException, NotAllowedException, TimeConflictException {
		
		return appointmentService.editAppointment(authentication.getName(), appointmentId, session_id);
	}
	
	
	@DeleteMapping("/appointments/{appointment_id}")
	private Appointment deleteAppointment(
			Authentication authentication,
			@PathVariable(name = "appointment_id") Integer appointmentId) throws NotFoundException, NotAllowedException, TimeConflictException {
		
		return appointmentService.deleteAppointment(authentication.getName(), appointmentId);
	}
}
