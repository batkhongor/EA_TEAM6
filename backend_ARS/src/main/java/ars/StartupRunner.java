package ars;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StartupRunner implements CommandLineRunner {
	
	//@Autowired
	//private personRepository personRepository;
	
    @Override
    @Transactional
    public void run(String...args) throws Exception {
    	
    	List<List<Integer>> result=new ArrayList<>();
    	//Person admin= new Person(1, "admin","1","1@gmail.com");
    	
    	//result.size()
    	//personRepository.save(admin);
    	
    	  int[] arr = {13, 7, 6, 45, 21, 9, 2, 100};
    	  
          // Sort subarray from index 1 to 4, i.e.,
          // only sort subarray {7, 6, 45, 21} and
          // keep other elements as it is.
          Arrays.sort(arr, 1, 5);
          
          Arrays.sort(arr, (List<Integer> a, List<Integer> b)-> a.get(0).compare(b.get(0)));
   
          System.out.printf("Modified arr[] : %s",
                            Arrays.toString(arr));
    }
}