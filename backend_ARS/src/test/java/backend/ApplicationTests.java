package backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import ars.Application;
import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.service.impl.ClientServiceImpl;
import ars.service.impl.PersonServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = Application.class)
class ApplicationTests {
	
	@Autowired
	private PersonServiceImpl personServiceImpl;
	
	@Autowired
	private ClientServiceImpl clientServiceImpl;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Test
	public void doesCreatePersonWork() {
		// to test if create function of PersonService works correct  
		Set<RoleType> roleTypes=new HashSet<>();
    	roleTypes.add( RoleType.ADMIN);
    	
    	String fname="John";
    	String lname="Admin";
    	String email="admin";
    	String password="admin";
    	
    	Person person1=new Person(fname, lname, email, password, roleTypes);
    	Person temp= personServiceImpl.createPerson(person1);
		
    	assertThat(person1).isEqualTo(temp);
    	assertThat(person1.getFirstname()).isEqualTo(fname);
    	assertThat(person1.getLastname()).isEqualTo(lname);
    	assertThat(person1.getEmail()).isEqualTo(email);
    	// check if number of roles matches
    	assertThat(person1.getRoles().size()).isEqualTo(roleTypes.size());
    	//check if password is encoded correctly
    	assertTrue(passwordEncoder.matches(password, temp.getPassword()));
    	
	}
	
	@Test
	public void doesDeletePersonWork() {
		// to test if create function of PersonService works correct  
		Set<RoleType> roleTypes=new HashSet<>();
    	roleTypes.add( RoleType.ADMIN);
    	
    	String fname="Khongor";
    	String lname="Khongor";
    	String email="khongor";
    	String password="khongor";
    	
    	Person person1=new Person(fname, lname, email, password, roleTypes);
    	Person temp= personServiceImpl.createPerson(person1);
    	//check if its created
    	assertThat(personServiceImpl.findById(person1.getId()).isPresent()).isEqualTo(true);
    	//check if its deleted
    	Integer id=person1.getId();
    	personServiceImpl.deletePerson(id);
    	assertFalse(personServiceImpl.findById(id).isPresent());
	}
	
	@Test
	public void doesUpdatePersonWork() {
		// to test if update function of PersonService works correct  
    	String lname="khongor";
    	String password="khongor";
    	
    	int personId=2;
    	//Person person1=new Person(fname, lname, email, password, roleTypes);
    	Person person1= personServiceImpl.findById(personId).orElse(null);
    	
    	person1.addRole(RoleType.CUSTOMER);
    	person1.setLastname(lname);
    	//person1.setPassword(password);
    	
    	personServiceImpl.updatePerson(person1);
    	
    	Person person2= personServiceImpl.findById(personId).orElse(null);
    	//check if it updated roles
    	assertThat(person1.getRoles().size()).isEqualTo(person2.getRoles().size());
    	//check if it updated Last name
    	assertThat(person1.getLastname()).isEqualTo(person2.getLastname());
    	//check if it updated password using encode
    	assertThat(person1.getPassword()).isEqualTo(person2.getPassword());
	}
	
	
}
