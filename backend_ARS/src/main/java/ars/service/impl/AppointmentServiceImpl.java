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

//SERVICE
@Service
public class AppointmentServiceImpl implements AppointmentService	 {
	@Autowired
	AppointmentRepository appointmentRepository;
	@Autowired
	SessionRepository sessionRepository;
	@Autowired
	PersonRepository personRepository;
	
<<<<<<< Updated upstream:backend_ARS/src/main/java/ars/service/impl/ClientServiceImpl.java
	@Override
	public List<Appointment> findAllClientAppointments(Integer ClientId){
		return appointmentRepository.findAll().stream().filter(a->a.getClient().getId()==ClientId)
					.collect(Collectors.toList());
	}
	@Override
	public void addNewAppointment(String email,Integer sessionId) throws IllegalAccessException {
		
		Person client = personRepository.findAll().stream().filter(p->p.getEmail().equals(email)).findFirst()
												.orElseThrow(()->new NoSuchElementException("No person with this id"));
		//Person client = personRepository.finb
=======
	
	@Override
	public List<Appointment> findAllClientAppointments(String email){
		return appointmentRepository.findAll().stream().filter(a->a.getClient().getEmail().equals(email)).collect(Collectors.toList());
	}
	@Override
	public Appointment createAppointment(String email,Integer sessionId) {
		
		Person client = personRepository.findAll().stream().filter(p->p.getEmail().equals(email)).findFirst().get();
		if(client==null) { 
			ret"!!ERROR!! No person with this id";
		}
>>>>>>> Stashed changes:backend_ARS/src/main/java/ars/service/impl/AppointmentServiceImpl.java
		
		if(	client.getRoles().stream().noneMatch(r->r.equals(RoleType.CUSTOMER))) { 
			throw new IllegalAccessException ("Only customers can create appointments");
		}
		
		Session requestedSession = sessionRepository.findById(sessionId).orElseThrow(()->new NoSuchElementException("No session with this id"));
	
		LocalDate currentDate = LocalDate.now();
		Appointment newAppointment = new Appointment(currentDate, client, requestedSession);
		
		if(requestedSession.getAppointmentRequests().isEmpty()) {
			newAppointment.setStatus(Status.CONFIRMED);
		}
		appointmentRepository.save(newAppointment);
		
	}
	@Override
<<<<<<< Updated upstream:backend_ARS/src/main/java/ars/service/impl/ClientServiceImpl.java
	public void deleteAppointment(Integer personId, Integer appointmentId) throws IllegalAccessException {
=======
	public void deleteAppointment(String email , Integer appointmentId) {
>>>>>>> Stashed changes:backend_ARS/src/main/java/ars/service/impl/AppointmentServiceImpl.java
	
		Person personTryingToDelete = personRepository.findById(personId).orElseThrow(()->new NoSuchElementException("No person with this id"));
		
		if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN)||r.equals(RoleType.CUSTOMER))) {
<<<<<<< Updated upstream:backend_ARS/src/main/java/ars/service/impl/ClientServiceImpl.java
			throw new IllegalAccessException("Only Admins and Clients can delete an appointment");
		}
		
		Appointment toDelete = appointmentRepository.findById(appointmentId)
				.orElseThrow(()->new NoSuchElementException("appointment does not exist in the records"));
=======
			throw new NotFoundException("!!ERROR!! Only Admins and Clients can delete an appointment");
		}
		
		Appointment toDelete = appointmentRepository.findById(appointmentId)
					.orElseThrow(()-> new NotFoundException("!!ERROR!! appointment with this id does not exist in the records"));
>>>>>>> Stashed changes:backend_ARS/src/main/java/ars/service/impl/AppointmentServiceImpl.java
		
		LocalDate appDate = toDelete.getSession().getDate();
		LocalTime appTime = LocalTime.of(toDelete.getSession().getStartTime(),0);
		LocalDateTime appDateTime = LocalDateTime.of(appDate,appTime);
	
		if(LocalDateTime.now().isAfter(appDateTime.minusHours(24))) {
			if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN))){
<<<<<<< Updated upstream:backend_ARS/src/main/java/ars/service/impl/ClientServiceImpl.java
				throw new RuntimeException("Only Admins can delete a an Appointment within 24hours of session");
=======
				throw new TimeConflictException("!!ERROR!! Only Admins can delete a an Appointment within 24hours of session");
>>>>>>> Stashed changes:backend_ARS/src/main/java/ars/service/impl/AppointmentServiceImpl.java
			}
		}
		//if trying to delete a confirmed appointment, then a new confirmed appointment must be picked
		if(toDelete.getStatus().equals(Status.CONFIRMED)) {
			toDelete.setStatus(Status.CANCELLED);
			try {
				pickNewConfirmedAppointment(toDelete.getSession().getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		appointmentRepository.delete(toDelete);
<<<<<<< Updated upstream:backend_ARS/src/main/java/ars/service/impl/ClientServiceImpl.java
	}
	@Override
	public void editAppointment(Integer appointmentId, LocalDate newDate, Integer newTime) {
		// TODO Auto-generated method stub
		Appointment appointmentToEdit = appointmentRepository.findById(appointmentId).orElseThrow(()-> new NoSuchElementException("Appointment does not exist"));
=======
		
		emailService.sendEmail(toDelete.getClient().getEmail(), "Appointment Deleted", "Appointment NO. "+appointmentId+" was Deleted");
		emailService.sendEmail(toDelete.getSession().getProvider().getEmail(), "Appointment Deleted", "Appointment NO. "+appointmentId+" was Deleted");
	}
	@Override
	public void editAppointment(String email, Integer appointmentId,Integer newSessionId) {
		Appointment appointmentToEdit = appointmentRepository.findById(appointmentId).get();
		Person person = personRepository.findByEmailOne(email);
>>>>>>> Stashed changes:backend_ARS/src/main/java/ars/service/impl/AppointmentServiceImpl.java
		
		Session newSession = sessionRepository.findAll().stream()
				.filter(s->s.getDate().equals(newDate))
				.filter(s->s.getStartTime()==newTime).findAny()
				.orElseThrow(()->new NoSuchElementException("No session found at this date/time"));
		
		appointmentToEdit.setSession(newSession);
		
		if(appointmentToEdit.getStatus().equals(Status.CONFIRMED)) {
			appointmentToEdit.setStatus(Status.PENDING);
			try {
				pickNewConfirmedAppointment(appointmentToEdit.getSession().getId());//pick a new confirmed for this session
			} catch (Exception e) { e.printStackTrace();}
		}
		
		appointmentRepository.save(appointmentToEdit);
<<<<<<< Updated upstream:backend_ARS/src/main/java/ars/service/impl/ClientServiceImpl.java
=======
		
		emailService.sendEmail(appointmentToEdit.getClient().getEmail(), "Appointment Edited", "Appointment Edited");
		emailService.sendEmail(appointmentToEdit.getSession().getProvider().getEmail(), "Appointment Edited", "Appointment Edited");
>>>>>>> Stashed changes:backend_ARS/src/main/java/ars/service/impl/AppointmentServiceImpl.java
	}
	@Override
	public  void pickNewConfirmedAppointment(Integer sessionId) throws Exception {
		// TODO Auto-generated method stub
		Session toEdit = sessionRepository.findById(sessionId).get();
		
		if(toEdit.getAppointmentRequests().stream().anyMatch(a->a.getStatus().equals(Status.CONFIRMED))) {
			throw new Exception("Session already has a confirmed appointment");
		}
		Appointment toConfirm = toEdit.getAppointmentRequests().stream()
									.filter(a->a.getStatus().equals(Status.PENDING))
									.sorted(Comparator.comparing(Appointment::getCreatedDate)).findFirst().get();
		toConfirm.setStatus(Status.CONFIRMED);
		toConfirm.setConfirmedDate(LocalDate.now());
		//Update the appointment list
		appointmentRepository.save(toConfirm);
	}

}
