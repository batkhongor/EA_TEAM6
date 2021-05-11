package ars.service;

import java.time.LocalDate;
import java.util.List;

import ars.domain.Appointment;
import ars.domain.Session;

public interface ClientService {

	List<Appointment> findAllClientAppointments(String email);
	
	void addNewAppointment( String email, Integer sessionId) throws IllegalAccessException;
	
	void deleteAppointment(Integer personId,Integer appointmentId) throws IllegalAccessException;
	
	void editAppointment(Integer appointmentId, LocalDate newDate, Integer newTime);
	
	void pickNewConfirmedAppointment(Integer sessionId) throws Exception;
	
	List<Session> findAllSessions();
	

}
