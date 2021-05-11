package ars.service;

import java.time.LocalDate;
import java.util.List;

import ars.domain.Appointment;
import ars.domain.Person;

public interface ClientService {

	List<Appointment> findAllClientAppointments(Integer ClientId);
	
	void addNewAppointment(Person client,LocalDate date, Integer timeInHours);
	
	void deleteAppointment(Integer appointmentId);
	
	void editAppointment(Integer appointmentId);
	

}
