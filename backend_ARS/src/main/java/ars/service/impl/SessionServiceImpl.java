package ars.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ars.domain.Person;
import ars.domain.Session;
import ars.exceptions.NotAllowedException;
import ars.exceptions.TimeConflictException;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import ars.service.SessionService;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private PersonRepository personRepository;

	@Override
	public List<Session> findAll(boolean futureOnly) {
		if (futureOnly) {
			return sessionRepository.findFutureSessions();
		} else {
			return sessionRepository.findAll();
		}
	}

	@Override
	public Page<Session> findAll(Pageable page, boolean futureOnly) {
		if (futureOnly) {
			return sessionRepository.findFutureSessions(page);
		} else {
			return sessionRepository.findAll(page);
		}
	}

	@Override
	public List<Session> findAllByEmail(String email, boolean futureOnly) {
		Person person = personRepository.findByEmailOne(email);

		if (futureOnly) {
			return sessionRepository.findFutureSessionsByProviderId(person.getId());
		} else {
			return sessionRepository.findSessionsByProviderId(person.getId());
		}
	}

	@Override
	public Session createSession(Session session, String providerEMail)
			throws TimeConflictException, NotAllowedException {
		Person person = personRepository.findByEmailOne(providerEMail);

		session.setProvider(person);

		

//		if(session.getDate())

		return sessionRepository.save(session);
	}

	@Override
	public Session updateSession(Integer sessionId, Session session, String providerEMail)
			throws TimeConflictException, NotAllowedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSession(Integer sessionId, String providerEMail) throws NotAllowedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSession(Integer sessionId) {
		// TODO Auto-generated method stub

	}

}
