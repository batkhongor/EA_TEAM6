package ars.repository;



import ars.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Integer> {
    public List<Person> findByEmail(String email);

    public default Person findByEmailOne(String email) {
        return findByEmail(email).get(0);

    }
}
