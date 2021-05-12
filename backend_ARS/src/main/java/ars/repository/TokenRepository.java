package ars.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Person;
import ars.domain.Token;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, String> {
	 
	public default List<Token> findByPerson(int personId) {
		return findByPerson(personId);
	}
}
