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

//SERVICE
@Service
public class ClientServiceImpl implements ClientService	 {
	@Autowired
	AppointmentRepository appointmentRepository;
	@Autowired
	SessionRepository sessionRepository;
	@Autowired
	PersonRepository personRepository;
	
	@Override
	public List<Session> findAllSessions(){
		return sessionRepository.findAll();
	}
	
	@Override
	public List<Appointment> findAllClientAppointments(String email){
//		Integer clientId = personRepository.findAll().stream().filter(p->p.getEmail().equals(email)).findFirst()
//				.orElseThrow(()->new NoSuchElementException("No person with this email")).getId();
		
		return appointmentRepository.findByClientEmail(email);
	}
	@Override
	public void addNewAppointment(String email,Integer sessionId) throws IllegalAccessException {
		
		Person client = personRepository.findAll().stream().filter(p->p.getEmail().equals(email)).findFirst()
												.orElseThrow(()->new NoSuchElementException("No person with this id"));
		
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
	public void deleteAppointment(Integer personId, Integer appointmentId) throws IllegalAccessException {
	
		Person personTryingToDelete = personRepository.findById(personId).orElseThrow(()->new NoSuchElementException("No person with this id"));
		
		if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN)||r.equals(RoleType.CUSTOMER))) {
			throw new IllegalAccessException("Only Admins and Clients can delete an appointment");
		}
		
		Appointment toDelete = appointmentRepository.findById(appointmentId)
				.orElseThrow(()->new NoSuchElementException("appointment does not exist in the records"));
		
		LocalDate appDate = toDelete.getSession().getDate();
		LocalTime appTime = LocalTime.of(toDelete.getSession().getStartTime(),0);
		LocalDateTime appDateTime = LocalDateTime.of(appDate,appTime);
	
		if(LocalDateTime.now().isAfter(appDateTime.minusHours(24))) {
			if(personTryingToDelete.getRoles().stream().noneMatch(r->r.equals(RoleType.ADMIN))){
				throw new RuntimeException("Only Admins can delete a an Appointment within 24hours of session");
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
	}
	@Override
	public void editAppointment(Integer appointmentId, LocalDate newDate, Integer newTime) {
		// TODO Auto-generated method stub
		Appointment appointmentToEdit = appointmentRepository.findById(appointmentId).orElseThrow(()-> new NoSuchElementException("Appointment does not exist"));
		
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

	@Override
	public List<Person> findAllClients() {
		// TODO Auto-generated method stub
		return personRepository.findAll().stream().filter(cl->cl.getRoles().stream().anyMatch(r->r.equals(RoleType.CUSTOMER))).collect(Collectors.toList());
	}

}
