package ars;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Person;
import ars.domain.RoleType;
import ars.service.PersonService;

@Component
public class StartupRunner implements CommandLineRunner {

	@Autowired
	private PersonService personService;

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		// List<List<Integer>> result=new ArrayList<>();
		Set<RoleType> roleTypes = new HashSet<>();
		roleTypes.add(RoleType.ADMIN);

		Person person1 = new Person("John", "Carter", "john", "john", roleTypes);
		Person person2 = new Person("Batdelger", "O", "batdelger@mail.com", "mypass", roleTypes);
		personService.createPerson(person1);
		personService.createPerson(person2);
		System.out.println(person1.getRoles().toString());
		// result.size()
		// personRepository.save(admin);
	}
}