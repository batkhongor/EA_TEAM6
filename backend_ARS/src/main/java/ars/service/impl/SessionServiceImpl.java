package ars.service.impl;

import java.util.List;

import ars.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ars.domain.Person;
import ars.domain.RoleType;
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

		if (!person.hasRole(RoleType.PROVIDER)) {
			throw new NotAllowedException("Not allowed to create a session");
		}
		session.setProvider(person);



		if(sessionRepository.findFutureSessions().stream()
				.filter(session1-> session1.getProvider()== session.getProvider()&&
						session1.getDate() ==session.getDate() &&
						session1.getStartTime()==session.getStartTime()).findAny()!=null)
		{
				throw new TimeConflictException("time is already occupied by another session");
		}
       /* else if(sessionRepository.findFutureSessions().stream()
				.filter(session1-> session1.getProvider()== session.getProvider()&&
						session1.getDate() ==session.getDate() &&
						(session1.getStartTime()+duration)==session.getStartTime()).findAny()!=null)*/


		return sessionRepository.save(session);
	}

	@Override
	public Session updateSession(Integer sessionId, Session session, String providerEMail)
			throws TimeConflictException, NotAllowedException, NotFoundException {
		// TODO Auto-generated method stub
		Session entity = sessionRepository.findById(sessionId).orElseThrow(new NotFoundException("Session is not found"));

		Person person = (Person) personRepository.findByEmailOne(providerEMail);
		Integer personId = person.getId();

		if (person.hasRole(RoleType.ADMIN)) {

			return sessionRepository.save(session);
		}
		else {
			if (entity.getProvider().getId() == personId) {
				throw new NotAllowedException("Not allowed to update this session");
			}
			if (sessionId.equals(session.getId())) {
				return sessionRepository.save(session);
			} else {
				throw new NotFoundException("Session missmatch");
			}
		}
	}

	@Override
	public void deleteSession(Integer sessionId, String providerEMail) throws NotAllowedException, NotFoundException {
		// TODO Auto-generated method stub

		Session entity = sessionRepository.findById(sessionId).orElseThrow(new NotFoundException("Session is not found"));

		Person person = (Person) personRepository.findByEmailOne(providerEMail);
		Integer personId = person.getId();
		if (person.hasRole(RoleType.ADMIN)) {

			sessionRepository.deleteById(sessionId);
		}
		else {
			if (entity.getProvider().getId() == personId) {

				sessionRepository.deleteById(sessionId);
			} else {
				throw new NotAllowedException("Not allowed to delete this session");
			}

		}
	}

	@Override
	public void deleteSession(Integer sessionId) {
		// TODO Auto-generated method stub

	}

}
