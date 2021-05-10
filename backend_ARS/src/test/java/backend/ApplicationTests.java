package backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ars.Application;
import ars.domain.Person;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = Application.class)
@AutoConfigureMockMvc
class ApplicationTests {

	@Test 
	void contextLoads() {
		
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
