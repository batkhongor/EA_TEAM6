package ars.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Session;

public interface AdminService {
	List<Session> findAllSessions();

	Page<Session> findAllSessions(Pageable pageable);
	
	public default Optional<Session> findSessionById(Integer sessionId) {
	    throw new NotYetImplementedException();
	}

	public default Session createSession(Session session) {
	    throw new NotYetImplementedException();
	}

	public default Session updateSession(Session session) {
	    throw new NotYetImplementedException();
	}

	public default void deleteSession(Integer sessionId) {
	    throw new NotYetImplementedException();
	}
}
