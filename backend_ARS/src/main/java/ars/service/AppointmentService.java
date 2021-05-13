package ars.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Appointment;
import ars.exceptions.NotAllowedException;
import ars.exceptions.NotFoundException;
import ars.exceptions.TimeConflictException;

public interface AppointmentService {

	Appointment createAppointment(String Email, Integer sessionId) throws NotFoundException, TimeConflictException;
	
	List<Appointment> findAllClientAppointments(String email);
	
	Appointment deleteAppointment(String email,Integer appointmentId) throws NotFoundException, NotAllowedException, TimeConflictException;
	
	Appointment editAppointment(String email, Integer appointmentId, Integer newSessionId) throws NotFoundException, NotAllowedException, TimeConflictException;
	
	Page<Appointment> findAllAppointments(Pageable pageable);
}
