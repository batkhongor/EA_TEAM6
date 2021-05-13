package ars;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.repository.SessionRepository;
import ars.service.EmailService;
import ars.service.PersonService;

@Component
public class StartupRunner implements CommandLineRunner {

	@Autowired
	private PersonService personServiceImpl;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private EmailService emailService;

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		// ------------ ADMIN ------------
		Person person1 = new Person("John", "Carter", "john@mail.com", "john", new HashSet<>());
		personServiceImpl.createPerson(person1);
		person1.addRole(RoleType.ADMIN);
		person1.addRole(RoleType.CUSTOMER);
		
		Person admin2 = new Person("Payman", "Salek", "psalek@mail.com", "payman", new HashSet<>());
		personServiceImpl.createPerson(admin2);
		admin2.addRole(RoleType.ADMIN);
		
		// ------------ PROVIDERS ------------
		Person provider1 = new Person("Provider", "Mr", "brukeabebe2@gmail.com", "john", new HashSet<>());
		provider1.addRole(RoleType.PROVIDER);
		personServiceImpl.createPerson(provider1);
		
		Person provider2 = new Person("Provider2", "Mr", "boazturya@gmail.com", "boaz", new HashSet<>());
		provider2.addRole(RoleType.PROVIDER);
		provider2.addRole(RoleType.CUSTOMER);
		personServiceImpl.createPerson(provider2);
		
		Person provider3 = new Person("mike", "mike", "mike@gmail.com", "mike", new HashSet<>());
		provider3.addRole(RoleType.PROVIDER);
		personServiceImpl.createPerson(provider3);

		List<Person> providersList = new ArrayList<>();
		providersList.addAll(Arrays.asList(provider1,provider2,provider3));

		LocalDate today = LocalDate.now();

		// ------------ RANDOM SESSIONS ------------// Assigning providers to sessions by picking them randomly from the list
		for (int time = 8; time < 16; time++) {
			Session session1 = new Session(today.plus((time-7), ChronoUnit.DAYS), LocalTime.of(time, 0), 30, "Iowa",
														providersList.get( (int)(Math.random()*providersList.size())) );
			sessionRepository.save(session1);
		}

		// ------------ CUSTOMER ------------
		Person customer1 = new Person("Customer", "Mr", "cs544eateam6@gmail.com", "john", new HashSet<>());
		customer1.addRole(RoleType.CUSTOMER);
		personServiceImpl.createPerson(customer1);

		// -------------Sending Startup Email-----------
				emailService.sendEmail("brukeabebe2@gmail.com", "APPLICATION STARTING", "Started on "+LocalDateTime.now());
	}

}