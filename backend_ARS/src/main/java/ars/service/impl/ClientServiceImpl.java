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
import ars.repository.AppointmentRepository;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import ars.service.ClientService;
import ars.service.EmailService;

//SERVICE
@Service
public class ClientServiceImpl implements ClientService	 {
	@Autowired
	AppointmentRepository appointmentRepository;
	@Autowired
	SessionRepository sessionRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	EmailService emailService;
	
	
	@Override
	public List<Session> findAllSessions(){
		return sessionRepository.findAll();
	}
	
	@Override
	public List<Appointment> findAllClientAppointments(String email){
		return appointmentRepository.findByClientEmail(email);
	}
	@Override
	public String addNewAppointment(String email,Integer sessionId) {
		
		Person client = personRepository.findAll().stream().filter(p->p.getEmail().equals(email)).findFirst()
												.orElseThrow(()->new NoSuchElementException("!!ERROR!! No person with this id"));
		
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
		return "SUCCESS";
		
	}
	@Override
	public String deleteAppointment(String email , Integer appointmentId) {
	
		Person personTryingToDelete = personRepository.findByEmailOne(email);
		if(personTryingToDelete==null) {return "!!ERROR!! No person with this id";}
		
		if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN)||r.equals(RoleType.CUSTOMER))) {
			return "!!ERROR. Only Admins and Clients can delete an appointment";
		}
		
		Appointment toDelete = appointmentRepository.findById(appointmentId)
				.orElseThrow(()->new NoSuchElementException("appointment does not exist in the records"));
		
		LocalDate appDate = toDelete.getSession().getDate();
		LocalTime appTime = LocalTime.of(toDelete.getSession().getStartTime(),0);
		LocalDateTime appDateTime = LocalDateTime.of(appDate,appTime);
	
		if(LocalDateTime.now().isAfter(appDateTime.minusHours(24))) {
			if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN))){
				return "!!ERROR.Only Admins can delete a an Appointment within 24hours of session";
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
		
		emailService.sendEmail(toDelete.getClient().getEmail(), "Appointment Deleted", "Appointment NO. "+appointmentId+" was Deleted");
		emailService.sendEmail(toDelete.getSession().getProvider().getEmail(), "Appointment Deleted", "Appointment NO. "+appointmentId+" was Deleted");
		return "Successfully Deleted";
	}
	@Override
	public String editAppointment(String email, Integer appointmentId,Integer newSessionId) {
		Appointment appointmentToEdit = appointmentRepository.findById(appointmentId).get();
		
		if(personRepository.findByEmailOne(email).getRoles().contains(RoleType.CUSTOMER)) {
			if(!appointmentToEdit.getClient().getEmail().equals(email)) {
				return "Client can only edit own appointment";
			}
		}
				
		if(appointmentToEdit==null) {return "Appointment does not exist";}
		Session newSession = sessionRepository.findById(newSessionId).get();
		if(newSession==null) {return "No Session with this id";}
		
		
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
		return "SUCCESS";
	}
		
	@Override
	public void  pickNewConfirmedAppointment(Integer sessionId) {
		Session toEdit = sessionRepository.findById(sessionId).get();
		
		Appointment toConfirm = toEdit.getAppointmentRequests().stream()
									.filter(a->a.getStatus().equals(Status.PENDING))
									.sorted(Comparator.comparing(Appointment::getCreatedDate)).findFirst().get();
		toConfirm.setStatus(Status.CONFIRMED);
		toConfirm.setConfirmedDate(LocalDate.now());

		appointmentRepository.save(toConfirm);
	}

	@Override
	public List<Person> findAllClients() {
		return personRepository.findAll().stream().filter(cl->cl.getRoles().contains(RoleType.CUSTOMER))
								.collect(Collectors.toList());
	}

}
