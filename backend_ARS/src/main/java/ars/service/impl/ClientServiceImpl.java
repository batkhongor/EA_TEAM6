package ars.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ars.domain.Appointment;
import ars.repository.AppointmentRepository;
import ars.service.ClientService;


@Service
public class ClientServiceImpl implements ClientService	 {
	AppointmentRepository appointmentRepository;
	
	public List<Appointment> findAllClientAppointments(Integer ClientId){
		return appointmentRepository.findAll().stream().filter(a->a.getClient().getId()==ClientId)
					.collect(Collectors.toList());
	}

}
