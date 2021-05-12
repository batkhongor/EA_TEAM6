package ars.service;

import ars.domain.Appointment;
import ars.exceptions.NotFoundException;

public interface AppointmentService {

	
	public Appointment createAppointment(Integer sessionId, Integer personId) throws NotFoundException;
}
