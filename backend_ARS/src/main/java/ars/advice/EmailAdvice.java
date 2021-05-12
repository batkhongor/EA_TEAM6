package ars.advice;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ars.domain.Appointment;
import ars.service.EmailService;

@Component
@Aspect
public class EmailAdvice {
	
	@Autowired
	EmailService emailService;
	
	@AfterReturning(pointcut="execution(* ars.service.AppointmentService.*(..))", returning="appointment")
	public void afterAppointmentEvent(JoinPoint joinpoint, Appointment appointment) {
		System.out.println("SENDING EMAIL");
		System.out.println("METHOD NAME:" + joinpoint.getSignature().getName());

		emailService.sendEmail(
			appointment.getClient().getEmail(), 
			"Appointment Created", 
			this.getTemplate(appointment)
		);
		
		emailService.sendEmail(
			appointment.getSession().getProvider().getEmail(),
			"Appointment Created", 
			this.getTemplate(appointment)
		);
	}
	
	
	private String getTemplate(Appointment appointment) {
		return "** Appointment Alert **"
				+ "\n ---------------------"
				+ "\n Provider: " + appointment.getSession().getProvider().getEmail()
				+ "\n Customer: " + appointment.getClient().getEmail()
				+ "\n Status: " + appointment.getStatus()
				+ "\n ---------------------";
	}
}
