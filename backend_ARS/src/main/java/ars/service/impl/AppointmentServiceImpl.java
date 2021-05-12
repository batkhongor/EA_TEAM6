package ars.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.domain.Status;
import ars.exceptions.NotAllowedException;
import ars.exceptions.NotFoundException;
import ars.exceptions.TimeConflictException;
import ars.repository.AppointmentRepository;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import ars.service.AppointmentService;
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
		return appointmentRepository.findAllByClientEmail(email);
	}
	
	@Override
	public Appointment createAppointment(String email,Integer sessionId) throws NotFoundException {
		
		Person client = personRepository.findByEmailOne(email);
		
		if(client==null) {
			throw new NotFoundException("!!ERROR!! No person with this id");
		}
		
		if(	client.getRoles().stream().noneMatch(r->r.equals(RoleType.CUSTOMER))) { 
			throw new NotFoundException("!!ERROR!! Only customers can create appointments");
		}
		
		Session requestedSession = sessionRepository.findById(sessionId).orElseThrow(()->new NotFoundException("!!ERROR!! No session with this id"));
	
		LocalDate currentDate = LocalDate.now();
		Appointment newAppointment = new Appointment(currentDate, client, requestedSession);
		
		if(requestedSession.getAppointmentRequests().isEmpty()) {
			newAppointment.setStatus(Status.CONFIRMED);
		}
		appointmentRepository.save(newAppointment);
		return newAppointment;
		
	}
	@Override
	public Appointment deleteAppointment(String email , Integer appointmentId) throws NotFoundException, NotAllowedException, TimeConflictException {
		Person personTryingToDelete = personRepository.findByEmailOne(email);
		if(personTryingToDelete==null) {
			throw new NotFoundException("!!ERROR!! No person with this id");
		}
		if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN)||r.equals(RoleType.CUSTOMER))) {
			throw new NotAllowedException("Only Admins and Clients can delete an appointment");
		}
		
		Appointment toDelete = appointmentRepository.findById(appointmentId)
					.orElseThrow(()-> new NotFoundException("!!ERROR!! appointment with this id does not exist in the records"));

		LocalDate appDate = toDelete.getSession().getDate();
		LocalTime appTime = toDelete.getSession().getStartTime();
		LocalDateTime appDateTime = LocalDateTime.of(appDate,appTime);
		
		if(appDateTime.isBefore(LocalDateTime.now())) {
			throw new TimeConflictException("Only future appointments can be deleted/edited");
		}
	
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
		return toDelete;
	}
	@Override
	public Appointment editAppointment(String personEmail, Integer appointmentId, Integer newSessionId) throws NotFoundException, NotAllowedException, TimeConflictException {
		Appointment appointmentToEdit = appointmentRepository.findById(appointmentId)
									.orElseThrow(()-> new NotFoundException("Appointment with this Id does not exist"));

		Person person = personRepository.findByEmailOne(personEmail);
				
		if(person.getRoles().contains(RoleType.CUSTOMER)) {
			if(!appointmentToEdit.getClient().getEmail().equals(personEmail)) {
				throw new NotAllowedException("!!ERROR!! Client can only edit own appointment");
			}
		}
				
		Session newSession = sessionRepository.findById(newSessionId)
									.orElseThrow(()->new NotFoundException("!!ERROR!! No Session with this id"));
		
		LocalDateTime newSessionDateTime = LocalDateTime.of(newSession.getDate(), newSession.getStartTime());
		
		if(newSessionDateTime.isBefore(LocalDateTime.now())) {
			throw new TimeConflictException("Only future appointments can be edited");
		}
	
		if(LocalDateTime.now().isAfter(newSessionDateTime.minusHours(24))) {
			if(person.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN))){
				throw new TimeConflictException("!!ERROR!! Less than 24hours before Session. Only Admins can make changes now");
			}
		}
		Integer currentSessionId = appointmentToEdit.getSession().getId();
		if(appointmentToEdit.getStatus().equals(Status.CONFIRMED)) {
			pickNewConfirmedAppointment(currentSessionId);
			
			appointmentToEdit.setStatus(Status.PENDING);
		}
		
		appointmentToEdit.setSession(newSession);
		appointmentRepository.save(appointmentToEdit);
		return appointmentToEdit;
	}
		
	@Override
	public void  pickNewConfirmedAppointment(Integer sessionId) throws NotAllowedException {
		Session toEdit = sessionRepository.findById(sessionId).get();
		
		if(toEdit.getAppointmentRequests().stream().anyMatch(a->a.getStatus().equals(Status.CONFIRMED))) {
			throw new NotAllowedException("Session already has a confirmed appointment");
		}
		
		Appointment toConfirm = toEdit.getAppointmentRequests().stream()
									.filter(a->a.getStatus().equals(Status.PENDING))
									.sorted(Comparator.comparing(Appointment::getCreatedDate).reversed()).findFirst().get();
		toConfirm.setStatus(Status.CONFIRMED);
		toConfirm.setConfirmedDate(LocalDate.now());

		appointmentRepository.save(toConfirm);
	}

}
