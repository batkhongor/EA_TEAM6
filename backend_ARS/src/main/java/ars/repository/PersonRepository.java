package ars.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ars.domain.Person;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Integer>{

}
