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
    	
    	Person person1=new Person("John", "Carter", "john", "john", roleTypes);
    	personServiceImpl.createPerson(person1);
    	System.out.println(person1.getRoles().toString());
    	//result.size()
    	//personRepository.save(admin);
    }
}