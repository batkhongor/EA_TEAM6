package ars.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.repository.AppointmentRepository;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import ars.service.ClientService;


@Service
public class ClientServiceImpl implements ClientService	 {
	@Autowired
	AppointmentRepository appointmentRepository;
	@Autowired
	SessionRepository sessionRepository;
	@Autowired
	PersonRepository personRepository;
	
	@Override
	public List<Appointment> findAllClientAppointments(Integer ClientId){
		return appointmentRepository.findAll().stream().filter(a->a.getClient().getId()==ClientId)
					.collect(Collectors.toList());
	}
	@Override
	public void addNewAppointment(Integer clientId,LocalDate date, Integer timeInHours) throws IllegalAccessException {
		
		Person client = personRepository.findById(clientId).orElseThrow(()->new NoSuchElementException("No person with this id"));
		
		if(	client.getRoles().stream().noneMatch(r->r.equals(RoleType.CUSTOMER))) { 
			throw new IllegalAccessException ("Only customers can create appointments");
		}
		
		Session requestedSession = sessionRepository.findAll().stream()
										.filter(s->s.getDate().equals(date))
										.filter(s->s.getStartTime()==timeInHours).findAny()
										.orElseThrow(()->new NoSuchElementException("No session found at this date/time"));
		
		LocalDate currentDate = LocalDate.now();
		Appointment newAppointment = new Appointment(currentDate, client, requestedSession);
		
		appointmentRepository.save(newAppointment);
		
	}
	@Override
	public void deleteAppointment(Integer appointmentId) throws IllegalAccessException {
		Appointment toDelete = appointmentRepository.findById(appointmentId)
				.orElseThrow(()->new NoSuchElementException("appointment does not exist in the records"));
		
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
		appointmentRepository.save(appointmentToEdit);
		
	}

}
