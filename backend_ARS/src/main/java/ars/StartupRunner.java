package ars;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Person;
import ars.domain.RoleType;
import ars.service.impl.PersonServiceImpl;

@Component
public class StartupRunner implements CommandLineRunner {
	
	@Autowired
	private PersonServiceImpl personServiceImpl;
	
    @Override
    @Transactional
    public void run(String...args) throws Exception {
    	
    	//List<List<Integer>> result=new ArrayList<>();
    	Set<RoleType> roleTypes=new HashSet<>();
    	roleTypes.add( RoleType.ADMIN);


		Set<RoleType> roleTypes1=new HashSet<>();
		roleTypes1.add( RoleType.PROVIDER);
    	Person person1=new Person("John", "Carter", "john", "john", roleTypes);
    	Person person2= new Person("Bruke", "Tadege", "brukeabebe2@gmail.com", "bruke", roleTypes1);
		Person person3= new Person("mike", "mike", "mike@gmail.com", "mike", roleTypes1);
    	personServiceImpl.createPerson(person1);
		personServiceImpl.createPerson(person2);
		personServiceImpl.createPerson(person3);
    	System.out.println(person1.getRoles().toString());
    	//result.size()
    	//personRepository.save(admin);
    }
}