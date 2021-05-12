package ars.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ars.domain.Appointment;
import ars.repository.AppointmentRepository;
import ars.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AppointmentRepository appointmentRepository;

	@Override
	public Page<Appointment> findAllAppointments(Pageable page) {
		return appointmentRepository.findAll(page);
	}

}
