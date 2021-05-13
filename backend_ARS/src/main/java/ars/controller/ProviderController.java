package ars.controller;

import ars.domain.*;
import ars.exceptions.NotAllowedException;
import ars.exceptions.NotFoundException;
import ars.exceptions.TimeConflictException;
import ars.repository.PersonRepository;
import ars.service.impl.ProviderServiceImpl;

import ars.service.impl.SessionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/provider")

public class ProviderController {


    @Autowired
    private ProviderServiceImpl providerServiceImpl;

    @Autowired
    private SessionServiceImpl sessionServiceImpl;


    @Autowired
    private PersonRepository personRepository;






//Get All appointments for a given session
    @GetMapping("/Appointments/{id}")
    public  List<Appointment> findAllAppointmentsForASession(@PathVariable(name="id") Integer SessionId, Authentication authentication) {
        return providerServiceImpl.findAllAppointmentsForASession(SessionId, authentication.getName());
    }


    //Get Confirmed Appointment for a given session
    @GetMapping("/Appointment/confirmed/{id}")
    public Optional<Appointment> findConfirmedAppointment(@PathVariable(name="id")Integer SessionId, Authentication authentication)
    {

        return providerServiceImpl.findConfirmedAppointment(SessionId, authentication.getName());

    }

    //List all sessions for a provider using a given provider ID
    @GetMapping("/sessions")
    public List<Session> findByEmail(Authentication authentication) {
        return sessionServiceImpl.findAllByEmail(authentication.getName(), false);
    }

    @GetMapping("/sessions/{id}")
    public Session getSession(@PathVariable("id") Integer sessionId) throws NotFoundException {
        Session entity = sessionServiceImpl.getSession(sessionId);
        return entity;
    }



    @PostMapping("/sessions")
    public Session createSession(@Valid @RequestBody Session session,Authentication authentication) throws TimeConflictException, NotAllowedException {

        return sessionServiceImpl.createSession(session,null, authentication.getName() );
    }


    @PutMapping("sessions/{id}")
    public Session updateSession(@PathVariable(name="id") Integer SessionId , @Valid @RequestBody Session session, Authentication authentication) throws NotFoundException, TimeConflictException, NotAllowedException {

        return sessionServiceImpl.updateSession(SessionId, session, null, authentication.getName());



    }

    @DeleteMapping("/sessions/{id}")
    void deleteSession(@PathVariable Integer id, Authentication authentication) throws NotFoundException, NotAllowedException {
        sessionServiceImpl.deleteSession(id, authentication.getName());
    }

    }

