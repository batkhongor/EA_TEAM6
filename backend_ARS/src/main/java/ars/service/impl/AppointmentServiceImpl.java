package ars.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service @Transactional
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
	public Appointment createAppointment(String email,Integer sessionId) throws NotFoundException, TimeConflictException {
		
		Person client = personRepository.findByEmailOne(email);
		
		if(client==null) { throw new NotFoundException("!!ERROR!! No person with this id");}
		
		if(!client.getRoles().contains(RoleType.CUSTOMER)) { 
			throw new NotFoundException("!!ERROR!! Only customers can create appointments");}
		
		Session requestedSession = sessionRepository.findById(sessionId)
									.orElseThrow(()->new NotFoundException("!!ERROR!! No session with this id"));
		
		LocalDateTime requestedSessionDateTime = LocalDateTime.of(requestedSession.getDate(),
																	requestedSession.getStartTime());
		
		if(requestedSessionDateTime.isBefore(LocalDateTime.now())) {
			throw new TimeConflictException("!!ERROR!! Can only create appointments for future sessions");
		}
	
		Appointment newAppointment = new Appointment(LocalDate.now(), LocalTime.now(),client, requestedSession);
		
		if(requestedSession.getAppointmentRequests().isEmpty()) {
			newAppointment.setStatus(Status.CONFIRMED);
			newAppointment.setConfirmedDate(LocalDate.now()); 
			newAppointment.setConfirmedTime(LocalTime.now());
		}
		appointmentRepository.save(newAppointment);
		return newAppointment;
	}
	
	@Override
	public Appointment deleteAppointment(String email , Integer appointmentId) throws NotFoundException, NotAllowedException, TimeConflictException {
		
		Person personTryingToDelete = personRepository.findByEmailOne(email);
		if(personTryingToDelete==null)	throw new NotFoundException("!!ERROR!! No person with this id");
		
		if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN)||r.equals(RoleType.CUSTOMER))) {
			throw new NotAllowedException("Only Admins and Clients can delete an appointment");
		}
		
		Appointment appointmentToDelete = appointmentRepository.findById(appointmentId)
					.orElseThrow(()-> new NotFoundException("!!ERROR!! appointment with this id does not exist in the records"));

		LocalDateTime appDateTime = LocalDateTime.of(appointmentToDelete.getSession().getDate() , appointmentToDelete.getSession().getStartTime());
		
		if(appDateTime.isBefore(LocalDateTime.now())) {
			throw new TimeConflictException("Only future appointments can be deleted/edited");
		}
	
		if( LocalDateTime.now().isAfter(appDateTime.minusHours(48)) &&
					!personTryingToDelete.getRoles().contains(RoleType.ADMIN) ){
				throw new TimeConflictException("!!ERROR!! Only Admins can delete a an Appointment within 48hours of session");
		}
		
		if(appointmentToDelete.getStatus().equals(Status.CONFIRMED)) {
			appointmentToDelete.setStatus(Status.CANCELLED);
			appointmentToDelete.setConfirmedDate(null); appointmentToDelete.setConfirmedTime(null);
			pickNewConfirmedAppointment(appointmentToDelete.getSession().getId());
		} else {
			appointmentToDelete.setStatus(Status.CANCELLED);
		}
		
		return appointmentToDelete;
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
	
		if(LocalDateTime.now().isAfter(newSessionDateTime.minusHours(48))) {
			if( !person.getRoles().contains(RoleType.ADMIN)){
				throw new TimeConflictException("!!ERROR!! Less than 48hours before Session. Only Admins can make changes now");
			}
		}
		Integer currentSessionId = appointmentToEdit.getSession().getId();
		if(appointmentToEdit.getStatus().equals(Status.CONFIRMED)) {
			appointmentToEdit.setStatus(Status.CANCELLED);
			appointmentToEdit.setConfirmedDate(null);  appointmentToEdit.setConfirmedTime(null); 
			pickNewConfirmedAppointment(currentSessionId);
		}
		
		appointmentToEdit.setSession(newSession);
		appointmentToEdit.setStatus(Status.PENDING);
		
		if(appointmentRepository.findAppointmentsBySessionId(newSessionId, Status.CONFIRMED).isEmpty()) {
			pickNewConfirmedAppointment(newSessionId);			
		}
		if(newSession.getAppointmentRequests().size()==1) {
			appointmentToEdit.setStatus(Status.CONFIRMED);
			appointmentToEdit.setConfirmedDate(LocalDate.now());  appointmentToEdit.setConfirmedTime(LocalTime.now());
		}
		appointmentRepository.save(appointmentToEdit);
		return appointmentToEdit;
	}
		
	
	public void  pickNewConfirmedAppointment(Integer sessionId) throws NotAllowedException {

		List<Appointment> confirmedAppointmentList = 
				appointmentRepository.findAppointmentsBySessionId(sessionId, Status.CONFIRMED);
		
		if (confirmedAppointmentList.size() >= 1) throw new NotAllowedException("Session already has a confirmed appointment");
		
		List<Appointment> pendingAppointmentList = 
				appointmentRepository.findAppointmentsBySessionId(sessionId, Status.PENDING);
		
		if (pendingAppointmentList.size() >= 1) {
			Appointment toConfirm = pendingAppointmentList.get(0);
			
			toConfirm.setStatus(Status.CONFIRMED);
			toConfirm.setConfirmedDate(LocalDate.now());
			toConfirm.setConfirmedTime(LocalTime.now());
			
			appointmentRepository.save(toConfirm);
		}
	}

	@Override
	public Page<Appointment> findAllAppointments(Pageable pageable) {
		return appointmentRepository.findAll(pageable);
	}


	
}
