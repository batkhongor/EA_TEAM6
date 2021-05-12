package ars.service;

import java.util.List;
import ars.domain.Appointment;
import ars.exceptions.NotFoundException;

public interface AppointmentService {

	Appointment createAppointment(String Email, Integer sessionId) throws NotFoundException;
	
	List<Appointment> findAllClientAppointments(String email);
	
	Appointment deleteAppointment(String email,Integer appointmentId);
	
	Appointment editAppointment(String email, Integer appointmentId, Integer newSessionId);
	
	void pickNewConfirmedAppointment(Integer sessionId); 
	
	
	
}
