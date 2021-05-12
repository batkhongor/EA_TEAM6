package ars.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ars.domain.Session;
import ars.repository.SessionRepository;
import ars.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private SessionRepository sessionRepository;

	public List<Session> findAllSessions() {
		return sessionRepository.findAll();
	}

	public Page<Session> findAllSessions(Pageable pageable) {
		return sessionRepository.findAll(pageable);
	}
	
	public  Optional<Session> findSessionById(Integer sessionId) {
	    return sessionRepository.findById(sessionId);
	}

	public  Session createSession(Session session) {
		return sessionRepository.save(session);
	}

	public Session updateSession(Session session) {
		return sessionRepository.save(session);
	}

	public void deleteSession(Integer sessionId) {
		sessionRepository.deleteById(sessionId);
	}
}
