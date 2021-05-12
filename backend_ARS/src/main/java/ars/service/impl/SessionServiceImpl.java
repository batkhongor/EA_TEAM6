package ars.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Session;
import ars.exceptions.NotAllowedException;
import ars.exceptions.TimeConflictException;
import ars.repository.SessionRepository;
import ars.service.SessionService;

public class SessionServiceImpl implements SessionService {

	@Autowired
	private SessionRepository sessionRepository;

	@Override
	public List<Session> findAll(boolean futureOnly) {
		if (futureOnly) {

		} else {
			return sessionRepository.findAll();
		}

		return null;
	}

	@Override
	public Page<Session> findAll(Pageable page, boolean futureOnly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Session> findAllByEmail(String email, boolean futureOnly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session createSession(Session session, Integer providerId)
			throws TimeConflictException, NotAllowedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session updateSession(Integer sessionId, Session session, Integer providerId)
			throws TimeConflictException, NotAllowedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSession(Integer sessionId, Integer providerId) throws NotAllowedException {
		// TODO Auto-generated method stub

	}

}
