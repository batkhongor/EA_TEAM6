package ars.service;

import ars.domain.Appointment;

public interface EmailService {

	void sendEmail(String to, String subject, String text);
	String getAppointmentTemplate(Appointment appointment);
}
