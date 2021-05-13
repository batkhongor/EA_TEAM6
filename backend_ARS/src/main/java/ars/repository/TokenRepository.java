package ars.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ars.domain.Token;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, String> {
	 
	@Query("from Token t where t.owner.id = :personId")
	List<Token> findByPerson(@Param("personId") int personId);
}
