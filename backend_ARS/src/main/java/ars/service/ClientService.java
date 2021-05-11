package ars.service;

import java.time.LocalDate;
import java.util.List;

import ars.domain.Appointment;
import ars.domain.Person;

public interface ClientService {

	List<Appointment> findAllClientAppointments(Integer ClientId);
	
	void addNewAppointment(Integer clientId,LocalDate date, Integer timeInHours) throws IllegalAccessException;
	
	void deleteAppointment(Integer personId,Integer appointmentId) throws IllegalAccessException;
	
	void editAppointment(Integer appointmentId, LocalDate newDate, Integer newTime);
	
	void pickNewConfirmedAppointment(Integer sessionId) throws Exception;
	

}
