package ars.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import ars.service.EmailService;
@Component
public class EmailServiceImp implements EmailService{

	@Autowired
	JavaMailSender jmsSender;
	
	@Override
	public void sendEmail(String to, String subject, String text) {
		   SimpleMailMessage message = new SimpleMailMessage(); 
	        message.setFrom("cs544eateam6@gmail.com");
	        message.setTo(to); 
	        message.setSubject(subject); 
	        message.setText(text);
	     jmsSender.send(message);
	}

}
