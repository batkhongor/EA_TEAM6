package ars.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Appointment;

public interface AdminService {
	Page<Appointment> findAllAppointments(Pageable page);

}
