package ars.service;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

public interface ProviderService {

    //List all sessions for a provider using a given provider ID

    public default List<Person> findAllProviders() {
        throw new NotYetImplementedException();
    }

    //List All appointments for a Given Session
    public default List<Appointment> findAllAppointmentsForASession(Integer SessionId) {
        throw new NotYetImplementedException();
    }
    public default Appointment findConfirmedAppointment(Integer SessionId)
    {
        throw new NotYetImplementedException();

    }


    public default List<Session> findSessionByEmail(Authentication authentication) {
        throw new NotYetImplementedException();
    }


    public default Session createSession(Session session, Authentication authentication) {


        throw new NotYetImplementedException();
    }


    public default Session updateSession(Integer SessionId, Session session, Authentication authentication) {
        throw new NotYetImplementedException();


    }

    public default void deleteSession(Integer id, Authentication authentication) {
        throw new NotYetImplementedException();
    }


}



