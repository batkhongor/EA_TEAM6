package ars;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.repository.SessionRepository;
import ars.service.PersonService;

@Component
public class StartupRunner implements CommandLineRunner {

	@Autowired
	private PersonService personServiceImpl;
	
	@Autowired
	private SessionRepository sessionRepo;
	
    @Override
    @Transactional
    public void run(String...args) throws Exception {
    	
    	//List<List<Integer>> result=new ArrayList<>();
    	
    	// ------------ ADMIN ------------
    	Set<RoleType> roleTypes=new HashSet<>();
    	roleTypes.add(RoleType.ADMIN);
    	roleTypes.add(RoleType.CUSTOMER);
    	
    	Person person1=new Person("John", "Carter", "john", "john", roleTypes);
    	personServiceImpl.createPerson(person1);
    	System.out.println(person1.getRoles().toString());
    	
    	// ------------ PROVIDER ------------
    	Person provider1 = new Person("Provider", "Mr", "provider", "john", new HashSet<>());
    	provider1.addRole(RoleType.PROVIDER);
    	personServiceImpl.createPerson(provider1);
    	
    	LocalDate today = LocalDate.now();
    	
    	// ------------ RANDOM SESSIONS ------------
    	for (int time=16; time < 24; time++) {
    		Session session1 = new Session(today.plus(time, ChronoUnit.DAYS), time, 30, "Iowa", provider1);
    		sessionRepo.save(session1);
    	}
    	
    	// ------------ CUSTOMER ------------
    	Person customer1 = new Person("Customer", "Mr", "customer", "john", new HashSet<>());
    	customer1.addRole(RoleType.CUSTOMER);
    	
    	personServiceImpl.createPerson(customer1);

		//-------------ADDITIONAL PROVIDERS FOR TESTING
		Set<RoleType> roleTypes1=new HashSet<>();
		roleTypes1.add( RoleType.PROVIDER);

		Person person2= new Person("Bruke", "Tadege", "brukeabebe2@gmail.com", "bruke", roleTypes1);
		Person person3= new Person("mike", "mike", "mike@gmail.com", "mike", roleTypes1);

		personServiceImpl.createPerson(person2);
		personServiceImpl.createPerson(person3);
    }









}