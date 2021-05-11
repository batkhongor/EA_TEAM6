package ars.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.Session;
import ars.repository.AppointmentRepository;
import ars.repository.SessionRepository;
import ars.service.ClientService;


@Service
public class ClientServiceImpl implements ClientService	 {
	@Autowired
	AppointmentRepository appointmentRepository;
	@Autowired
	SessionRepository sessionRepository;
	
	@Override
	public List<Appointment> findAllClientAppointments(Integer ClientId){
		return appointmentRepository.findAll().stream().filter(a->a.getClient().getId()==ClientId)
					.collect(Collectors.toList());
	}
	@Override
	public void addNewAppointment(Person client,LocalDate date, Integer timeInHours) {
	
		Session requestedSession = sessionRepository.findAll().stream()
										.filter(s->s.getDate().equals(date))
										.filter(s->s.getStartTime()==timeInHours).findAny()
										.orElseThrow(()->new NoSuchElementException("No session found at this date/time"));
		LocalDate currentDate = LocalDate.now();
		Appointment newAppointment = new Appointment(currentDate, client, requestedSession);
		
		appointmentRepository.save(newAppointment);
		
	}
	@Override
	public void deleteAppointment(Integer appointmentId) {
		Appointment toDelete = appointmentRepository.findById(appointmentId)
				.orElseThrow(()->new NoSuchElementException("appointment does not exist in the records"));
		
		appointmentRepository.delete(toDelete);
		
	}
	@Override
	public void editAppointment(Integer appointmentId) {
		// TODO Auto-generated method stub
		
	}

}
