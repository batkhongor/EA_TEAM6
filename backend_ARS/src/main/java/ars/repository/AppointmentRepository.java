package ars.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Appointment;
import ars.domain.Status;

@Repository
@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

	@Query("select a from Appointment a where a.client.email = :email")
	public List<Appointment> findAllByClientEmail(String email);
	
	@Query("select a from Appointment a "
			+ "where a.session.id = :sessionId and a.status = :status "
			+ "order by a.createdDate, a.createdTime ")
	public List<Appointment> findAppointmentsBySessionId(Integer sessionId, Status status);
	
	@Query("select a from Appointment a "
			+ "where a.session.id = :sessionId "
			+ "order by a.createdDate, a.createdTime ")
	public List<Appointment> findAllAppointmentsBySessionId(Integer sessionId);
}
