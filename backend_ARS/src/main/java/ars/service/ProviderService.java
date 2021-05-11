package ars.service;


import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {



    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionRepository sessionRepository;


    public List<Person> findAllProviders() {
        return personRepository.findAll().stream().filter(person -> person.getRoles()
                .contains(RoleType.PROVIDER)).collect(Collectors.toList());
    }

    //List all sessions for a provider using a given provider ID

    public List<Session> findSessionById(Integer personId) {
        return sessionRepository.findAll().stream().filter(session -> session.getProvider().getId()==personId)
                .collect(Collectors.toList());
    }




    public Session createSession(Session session, Integer providerId) {


          if(session.getProvider().getId()==providerId) {
              return sessionRepository.save(session);
          }

          else
          {
              throw new RuntimeException();
          }
    }



    public Session updateSession(Integer SessionId , Session session) {
        Session entity =sessionRepository.findById(SessionId).orElseThrow(RuntimeException::new);

        if(SessionId.equals(session.getId()))
        {
            return sessionRepository.save(session);
        }
        else
        {
            return null;
        }


    }


    public void deleteSession(Integer id) {
        sessionRepository.deleteById(id);
    }
}
