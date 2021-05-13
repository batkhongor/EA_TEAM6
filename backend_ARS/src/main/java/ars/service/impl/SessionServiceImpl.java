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


    //Used for the Admin
    @Override
    public List<Session> findSessionForProvider(Integer ProviderId, String email) throws NotFoundException, NotAllowedException {

        Person person = (Person) personRepository.findByEmailOne(email);
        if (personRepository.findById(ProviderId).isPresent()) {
            if (person.hasRole(RoleType.ADMIN)) {
                return sessionRepository.findSessionsByProviderId(ProviderId);
            } else {
                throw new NotAllowedException("not allowed to see list of sessions");
            }
        } else {
            throw new NotFoundException(("Provider Not found"));
        }

    }

    @Override
    public Session getSession(Integer sessionId) throws NotFoundException {
        return null;
    }

    @Override
    public Session createSession(Session session, String providerEmail, String personEMail)
            throws TimeConflictException, NotAllowedException {

        Person person = personRepository.findByEmailOne(personEMail);

        if (person.hasRole(RoleType.ADMIN)) {
            Person provider = personRepository.findByEmailOne(providerEmail);
            Integer providerId = provider.getId();
            List<Session> futureSession = sessionRepository.findFutureSessionsByProviderId(providerId);
            List<Session> conflictingSession = futureSession.stream().
                    filter(session1 -> session1.getDate().isEqual(session.getDate()) &&
                            session1.getStartTime().equals(session.getStartTime())).collect(Collectors.toList());

            if (conflictingSession.isEmpty()) {

                session.setProvider(provider);
                return sessionRepository.save(session);
            } else {
                throw new TimeConflictException("Session Time already occupied");
            }
        } else {

            Integer providerId = person.getId();
            List<Session> futureSession = sessionRepository.findFutureSessionsByProviderId(providerId);
            List<Session> conflictingSession = futureSession.stream().
                    filter(session1 -> session1.getDate().isEqual(session.getDate()) &&
                            session1.getStartTime().equals(session.getStartTime())).collect(Collectors.toList());

            session.setProvider(person);

            if (conflictingSession.isEmpty()) {
                return sessionRepository.save(session);
            } else {
                throw new TimeConflictException("Session Time already occupied");
            }

        }

    }


    @Override
    public Session updateSession(Integer sessionId, Session session, String providerEMail, String personEMail)
            throws TimeConflictException, NotAllowedException, NotFoundException {
        // TODO Auto-generated method stub
        Session entity = sessionRepository.findById(sessionId).orElseThrow(new NotFoundException("Session is not found"));
        session.setId(sessionId);
        Person person = (Person) personRepository.findByEmailOne(personEMail);
        Integer personId = person.getId();

        if (person.hasRole(RoleType.ADMIN)) {
            Person provider = personRepository.findByEmailOne(providerEMail);
            session.setProvider(provider);
            Integer providerId = entity.getProvider().getId();
            List<Session> futureSession = sessionRepository.findFutureSessionsByProviderId(providerId);

            List<Session> conflictingSession = futureSession.stream().
                    filter(session1 -> session1.getDate().isEqual(session.getDate()) &&
                            session1.getStartTime().equals(session.getStartTime())).collect(Collectors.toList());

            if (conflictingSession.isEmpty()) {
                return sessionRepository.save(session);
            } else {
                throw new TimeConflictException("Session Time already occupied");
            }
        } else {

            Person provider = person;
            Integer providerId = provider.getId();
            List<Session> futureSession = sessionRepository.findFutureSessionsByProviderId(providerId);

            List<Session> conflictingSession = futureSession.stream().
                    filter(session1 -> session1.getDate().isEqual(session.getDate()) &&
                            session1.getStartTime().equals(session.getStartTime())).collect(Collectors.toList());

            if (entity.getProvider().getId() != personId) {
                throw new NotAllowedException("Not allowed to update this session");
            }
            if (sessionId.equals(session.getId())) {

                session.setProvider(provider);
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
        } else {
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
