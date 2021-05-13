package ars.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

		Person person = (Person) personRepository.findByEmailOne(providerEMail);

		if (!person.hasRole(RoleType.PROVIDER)) {
			throw new NotAllowedException("Not allowed to create a session");
	}

		if (person.hasRole(RoleType.ADMIN)) {

			Integer providerId= session.getProvider().getId();
			List<Session> futureSession= sessionRepository.findFutureSessionsByProviderId(providerId);
			List<Session> conflictingSession=futureSession.stream().
					filter(session1->session1.getDate()==session.getDate() &&
					session1.getStartTime()==session.getStartTime()).collect(Collectors.toList());


			if(conflictingSession.isEmpty())
			{
				return sessionRepository.save(session);
			}

			else
			{
				throw new TimeConflictException("Session Time already occupied");
			}
		}

		else{

			session.setProvider(person);
		List<Session> futureSession= sessionRepository.findFutureSessions();
		//List<Session> futureSession1= futureSession.stream().filter(session1->session1.getProvider().getId()==person.getId()).collect(Collectors.toList());
			List<Session> futureSession1= sessionRepository.findFutureSessionsByProviderId(person.getId());


		List<Session> conflictingSession=futureSession1.stream().filter(session1->session1.getDate() ==session.getDate() &&
				session1.getStartTime()==session.getStartTime()).collect(Collectors.toList());
		if(conflictingSession.isEmpty())
			{
				return sessionRepository.save(session);
			}

				else
				{
					throw new TimeConflictException("Session Time already occupied");
				}


		}



	}


	@Override
	public Session updateSession(Integer sessionId, Session session, String providerEMail)
			throws TimeConflictException, NotAllowedException, NotFoundException {
		// TODO Auto-generated method stub
		Session entity = sessionRepository.findById(sessionId).orElseThrow(new NotFoundException("Session is not found"));

		Person person = (Person) personRepository.findByEmailOne(providerEMail);
		Integer personId = person.getId();

		if (person.hasRole(RoleType.ADMIN)) {

			Integer providerId= session.getProvider().getId();
			List<Session> futureSession= sessionRepository.findFutureSessionsByProviderId(providerId);
			List<Session> conflictingSession=futureSession.stream().
					filter(session1->session1.getDate()==session.getDate() &&
							session1.getStartTime()==session.getStartTime()).collect(Collectors.toList());


			if(conflictingSession.isEmpty())
			{
				return sessionRepository.save(session);
			}

			else
			{
				throw new TimeConflictException("Session Time already occupied");
			}
		}
		else {
			if (entity.getProvider().getId() != personId) {
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
		Person person = (Person) personRepository.findByEmailOne(providerEMail);
		Session session = sessionRepository.findById(sessionId).orElseThrow(new NotFoundException("Session is not found"));
		Integer personId = person.getId();
		if (person.hasRole(RoleType.ADMIN)) {

			sessionRepository.deleteById(sessionId);
		}

		else {
			if (session.getProvider().getId() == personId) {
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
