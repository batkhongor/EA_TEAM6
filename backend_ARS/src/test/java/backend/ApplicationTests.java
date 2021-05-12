package backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ars.Application;
import ars.domain.Person;
<<<<<<< Updated upstream
=======
import ars.domain.RoleType;
import ars.domain.Session;
import ars.service.impl.AppointmentServiceImpl;
import ars.service.impl.PersonServiceImpl;
>>>>>>> Stashed changes

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = Application.class)
@AutoConfigureMockMvc
class ApplicationTests {
<<<<<<< Updated upstream

	@Test 
	void contextLoads() {
=======
	
	@Autowired
	private PersonServiceImpl personServiceImpl;
	
	@Autowired
	private AppointmentServiceImpl clientServiceImpl;
	
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
>>>>>>> Stashed changes
		
	}
	
	
	@Test
	public void doesCostructorWork() {
	    String name = "alex";
	    Person person = new Person();
	    person.setFirstname(name);
	    
	     assertThat(person.getFirstname())
	      .isEqualTo(name);
	 }
}
