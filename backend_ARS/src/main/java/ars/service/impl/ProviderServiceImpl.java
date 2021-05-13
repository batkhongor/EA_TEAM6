package ars.service.impl;

import ars.domain.*;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import ars.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class ProviderServiceImpl implements ProviderService {


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionRepository sessionRepository;

    //List all sessions for a provider using a given provider ID
    public List<Person> findAllProviders() {
        return personRepository.findAll().stream().filter(person -> person.getRoles()
                .contains(RoleType.PROVIDER)).collect(Collectors.toList());
    }

    public List<Appointment> findAllAppointmentsForASession(Integer SessionId, String email) {


        Person provider = (Person) personRepository.findByEmailOne(email);
        Integer providerId = provider.getId();
        if (providerId == SessionId) {

            return sessionRepository.findById(SessionId).get().getAppointmentRequests();
        } else {
            throw new RuntimeException();
        }

    }

    public Optional<Appointment> findConfirmedAppointment(Integer SessionId, String email) {

        Person provider = (Person) personRepository.findByEmailOne(email);
        Integer providerId = provider.getId();

        if (providerId == SessionId) {
            return sessionRepository.findById(SessionId).get().getAppointmentRequests().stream()
                    .filter(appointment -> appointment.getStatus() == Status.CONFIRMED).findAny();
        } else {
            throw new RuntimeException();
        }

    }


    // find all sessions for the given provider
    public List<Session> findSessionByEmail(String email) {


        Person provider = (Person) personRepository.findByEmailOne(email);
        Integer providerId = provider.getId();
        return sessionRepository.findAll().stream().filter(session -> session.getProvider().getId() == providerId)
                .collect(Collectors.toList());
    }


    public Session createSession(Session session, String email) {


        Person provider = (Person) personRepository.findByEmailOne(email);
        Integer providerId = provider.getId();


        if (session.getProvider().getId() == providerId) {
            return sessionRepository.save(session);
        } else {
            throw new RuntimeException();
        }
    }


    public Session updateSession(Integer SessionId, Session session, String email) {


        Person provider = (Person) personRepository.findByEmailOne(email);
        Integer providerId = provider.getId();

        Session entity = sessionRepository.findById(SessionId).orElseThrow(RuntimeException::new);

        if (SessionId.equals(session.getId()) & sessionRepository.findById(SessionId).get().getProvider().getId() == providerId) {
            return sessionRepository.save(session);
        } else {
            throw new RuntimeException();
        }


    }


    public void deleteSession(Integer id, String email) {

        Person provider = (Person) personRepository.findByEmailOne(email);
        Integer providerId = provider.getId();

        if (sessionRepository.findById(id).get().getProvider().getId() == providerId) {

            sessionRepository.deleteById(id);
        } else {
            throw new RuntimeException();
        }

    }
}
