package ars.controller;

import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.repository.PersonRepository;
import ars.repository.SessionRepository;
import ars.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/provider")

public class ProviderController {


    @Autowired
    private ProviderService providerService;


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionRepository sessionRepository;

    //get all providers
    @GetMapping
    public List<Person> findAllProviders() {
        return personRepository.findAll().stream().filter(person -> person.getRoles()
                                          .contains(RoleType.PROVIDER)).collect(Collectors.toList());
    }

    //List all sessions for a provider using a given provider ID
    @GetMapping("/sessions/{id}")
    public List<Session> findById(@PathVariable(name = "id") Integer SessionId) {
        return sessionRepository.findAll().stream().filter(session -> session.getProvider().getId()==SessionId)
                .collect(Collectors.toList());
    }






    @PostMapping("/sessions")
    public Session createSession(@Valid @RequestBody Session session) {
       /* sessionRepository.findById(session.getId())
                .ifPresent(c->{
                    throw new RuntimeException();

                });
                */


        return sessionRepository.save(session);
    }


    @PutMapping("sessions/{id}")
    public Session updateSession(@PathVariable(name="id") Integer SessionId , @Valid @RequestBody Session session) {
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

    @DeleteMapping("/sessions/{id}")
    void deleteSession(@PathVariable Integer id) {
        sessionRepository.deleteById(id);
    }

    }

