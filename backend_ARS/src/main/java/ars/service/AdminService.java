package ars.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Appointment;
import ars.domain.Session;

public interface AdminService {
	Page<Appointment> findAllAppointments(Pageable page);
}
