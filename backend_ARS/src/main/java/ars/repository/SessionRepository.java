package ars.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Session;

@Repository
@Transactional
public interface SessionRepository extends JpaRepository<Session, Integer> {
	@Query("from Session s where s.date >= current_date()")
	List<Session> findFutureSessions();

	@Query("from Session s where s.date >= current_date()")
	Page<Session> findFutureSessions(Pageable page);

	@Query("from Session s where s.date >= current_date() and s.provider.id = :providerId")
	List<Session> findFutureSessionsByProviderId(@Param("providerId") Integer providerId);

	@Query("from Session s where s.provider.id = :providerId")
	List<Session> findSessionsByProviderId(@Param("providerId") Integer providerId);
}
