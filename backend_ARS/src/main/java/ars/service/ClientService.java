package ars.service;

import java.time.LocalDate;
import java.util.List;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.Session;

public interface ClientService {

	List<Appointment> findAllClientAppointments(String email);
	
	String addNewAppointment( String email, Integer sessionId); //throws IllegalAccessException;
	
	String deleteAppointment(String email,Integer appointmentId); //throws IllegalAccessException;
	
	String editAppointment(String email, Integer appointmentId, Integer newSessionId);
	
	void pickNewConfirmedAppointment(Integer sessionId); //throws Exception;
	
	List<Session> findAllSessions();
	
	List<Person> findAllClients();
	

}
