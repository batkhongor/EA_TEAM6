package ars.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.domain.Status;
import ars.exceptions.NotFoundException;
import ars.exceptions.TimeConflictException;
import ars.repository.AppointmentRepository;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import ars.service.AppointmentService;
import ars.service.ClientService;
import ars.service.EmailService;

//SERVICE
@Service
public class AppointmentServiceImpl implements AppointmentService	 {
	@Autowired
	AppointmentRepository appointmentRepository;
	@Autowired
	SessionRepository sessionRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	EmailService emailService;
	
	
	
	@Override
	public List<Appointment> findAllClientAppointments(String email){
		return appointmentRepository.findAll().stream().filter(a->a.getClient().getEmail().equals(email)).collect(Collectors.toList());
	}
	@Override
	public Appointment createAppointment(String email,Integer sessionId) {
		
		Person client = personRepository.findAll().stream().filter(p->p.getEmail().equals(email)).findFirst().!!ERROR!! No person with this id";
		}
		
		if(	client.getRoles().stream().noneMatch(r->r.equals(RoleType.CUSTOMER))) { 
			return "!!ERROR!! Only customers can create appointments";
		}
		
		Session requestedSession = sessionRepository.findById(sessionId).get();
		if(requestedSession==null) {
			return "!!ERROR!! No session with this id";
		}
	
		LocalDate currentDate = LocalDate.now();
		Appointment newAppointment = new Appointment(currentDate, client, requestedSession);
		
		if(requestedSession.getAppointmentRequests().isEmpty()) {
			newAppointment.setStatus(Status.CONFIRMED);
		}
		appointmentRepository.save(newAppointment);
		return "SUCCESSFULLY ADDED";
		
	}
	@Override
	public void deleteAppointment(String email , Integer appointmentId) {
		Person personTryingToDelete = personRepository.findByEmailOne(email);
		if(personTryingToDelete==null) {
			return "!!ERROR!! No person with this id";
		}
		if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN)||r.equals(RoleType.CUSTOMER))) {
			throw new IllegalAccessException("Only Admins and Clients can delete an appointment");
		}
		

		
		Appointment toDelete = appointmentRepository.findById(appointmentId)
					.orElseThrow(()-> new NotFoundException("!!ERROR!! appointment with this id does not exist in the records"));

		
		LocalDate appDate = toDelete.getSession().getDate();
		LocalTime appTime = LocalTime.of(toDelete.getSession().getStartTime(),0);
		LocalDateTime appDateTime = LocalDateTime.of(appDate,appTime);
	
		if(LocalDateTime.now().isAfter(appDateTime.minusHours(24))) {
			if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN))){
				throw new TimeConflictException("!!ERROR!! Only Admins can delete a an Appointment within 24hours of session");
			}
		}
		if(toDelete.getStatus().equals(Status.CONFIRMED)) {
			toDelete.setStatus(Status.CANCELLED);
			try {
				pickNewConfirmedAppointment(toDelete.getSession().getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		appointmentRepository.delete(toDelete);
	}
	@Override
	public void editAppointment(Integer appointmentId, LocalDate newDate, Integer newTime) {
		// TODO Auto-generated method stub
		Appointment appointmentToEdit = appointmentRepository.findById(appointmentId).orElseThrow(()-> new NoSuchElementException("Appointment does not exist"));

		Appointment appointmentToEdit = appointmentRepository.findById(appointmentId).get();
		Person person = personRepository.findByEmailOne(email);
		
		if(appointmentToEdit==null) {return "!!ERROR!! Appointment does not exist";}
		
		if(person.getRoles().contains(RoleType.CUSTOMER)) {
			if(!appointmentToEdit.getClient().getEmail().equals(email)) {
				return "!!ERROR!! Client can only edit own appointment";
			}
		}
				
		Session newSession = sessionRepository.findById(newSessionId).get();
		if(newSession==null) {return "!!ERROR!! No Session with this id";}
		
		LocalDateTime newSessionDateTime = LocalDateTime.of(newSession.getDate(), LocalTime.of(newSession.getStartTime(),0));
	
		if(LocalDateTime.now().isAfter(newSessionDateTime.minusHours(24))) {
			if(person.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN))){
				return "!!ERROR!! Less than 24hours before Session. Only Admins can make changes now";
			}
		}
		
		appointmentToEdit.setSession(newSession);
		
		if(appointmentToEdit.getStatus().equals(Status.CONFIRMED)) {
			appointmentToEdit.setStatus(Status.PENDING);
			try {
				pickNewConfirmedAppointment(appointmentToEdit.getSession().getId());//pick a new confirmed for this session
			} catch (Exception e) { e.printStackTrace();}
		}
		
		appointmentRepository.save(appointmentToEdit);
		
		emailService.sendEmail(appointmentToEdit.getClient().getEmail(), "Appointment Edited", "Appointment Edited");
		emailService.sendEmail(appointmentToEdit.getSession().getProvider().getEmail(), "Appointment Edited", "Appointment Edited");
		return "SUCCESSFULLY EDITED";
	}
		
	@Override
	public void  pickNewConfirmedAppointment(Integer sessionId) {
		Session toEdit = sessionRepository.findById(sessionId).get();
		
		Appointment toConfirm = toEdit.getAppointmentRequests().stream()
									.filter(a->a.getStatus().equals(Status.PENDING))
									.sorted(Comparator.comparing(Appointment::getCreatedDate).reversed()).findFirst().get();
		toConfirm.setStatus(Status.CONFIRMED);
		toConfirm.setConfirmedDate(LocalDate.now());

		appointmentRepository.save(toConfirm);
	}

}
