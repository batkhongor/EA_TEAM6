package ars.service;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.Session;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface ProviderService {

    //List all sessions for a provider using a given provider ID

    public default List<Person> findAllProviders() {
        throw new NotYetImplementedException();
    }

    //List All appointments for a Given Session
    public default List<Appointment> findAllAppointmentsForASession(Integer SessionId) {
        throw new NotYetImplementedException();
    }
    public default Optional<Appointment> findConfirmedAppointment(Integer SessionId)
    {
        throw new NotYetImplementedException();

    }


    public default List<Session> findSessionByEmail(String Email) {
        throw new NotYetImplementedException();
    }


    public default Session createSession(Session session, String Email) {


        throw new NotYetImplementedException();
    }


    public default Session updateSession(Integer SessionId, Session session, String Email) {
        throw new NotYetImplementedException();


    }

    public default void deleteSession(Integer id, String Email) {
        throw new NotYetImplementedException();
    }


}


