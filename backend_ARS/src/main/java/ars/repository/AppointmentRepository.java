package ars.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Appointment;

@Repository
@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{
	
	@Query("select a from Appointment a where a.client.id = :id")
	List<Appointment> findByClientId(@Param("id") Integer id);
	
	@Query("select a from Appointment a where a.client.email = :email")
	List<Appointment> findByClientEmail(@Param("email") String email);
}
