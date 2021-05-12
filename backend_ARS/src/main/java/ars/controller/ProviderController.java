package ars.controller;

import ars.domain.*;
import ars.repository.PersonRepository;
import ars.service.impl.ProviderServiceImpl;

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
    private PersonRepository personRepository;






//Get All appointments for a given session
    @GetMapping("/Appointments/{id}")
    public  List<Appointment> findAllAppointmentsForASession(@PathVariable(name="id") Integer SessionId, Authentication authentication) {
        return providerServiceImpl.findAllAppointmentsForASession(SessionId, authentication);
    }


    //Get Confirmed Appointment for a given session
    @GetMapping("/Appointment/confirmed/{id}")
    public Optional<Appointment> findConfirmedAppointment(@PathVariable(name="id")Integer SessionId, Authentication authentication)
    {

        return providerServiceImpl.findConfirmedAppointment(SessionId,  authentication);

    }

    //List all sessions for a provider using a given provider ID
    @GetMapping("/sessions")
    public List<Session> findByEmail(Authentication authentication) {
        return providerServiceImpl.findSessionByEmail(authentication);
    }



    @PostMapping("/sessions")
    public Session createSession(@Valid @RequestBody Session session,Authentication authentication) {

        return providerServiceImpl.createSession(session,authentication );
    }


    @PutMapping("sessions/{id}")
    public Session updateSession(@PathVariable(name="id") Integer SessionId , @Valid @RequestBody Session session, Authentication authentication) {

        return providerServiceImpl.updateSession(SessionId, session, authentication);



    }

    @DeleteMapping("/sessions/{id}")
    void deleteSession(@PathVariable Integer id, Authentication authentication) {
                providerServiceImpl.deleteSession(id, authentication);
    }

    }

