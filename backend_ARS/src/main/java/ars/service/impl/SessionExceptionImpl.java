package ars.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Session;
import ars.exceptions.NotAllowedException;
import ars.exceptions.TimeConflictException;
import ars.service.SessionService;

public class SessionExceptionImpl implements SessionService {

	@Override
	public List<Session> findAll(boolean futureOnly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Session> findAll(Pageable page, boolean futureOnly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session createSession(Session session, Integer personId) throws TimeConflictException, NotAllowedException {
		// TODO Auto-generated method stub
		return null;
	}

}
