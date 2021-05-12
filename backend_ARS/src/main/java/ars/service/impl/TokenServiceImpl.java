package ars.service.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ars.domain.Token;
import ars.repository.PersonRepository;
import ars.repository.TokenRepository;
import ars.service.PersonService;
import ars.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private TokenRepository tokenRepository;

	public List<Token> findAll() {
		return tokenRepository.findAll();
	}
	
	public List<Token> findAllTokensByPerson(Integer personId) {
		return tokenRepository.findByPerson(personId);
	}

	public Optional<Token> findById(String token) {
		return tokenRepository.findById(token);
	}
	
	public Token createToken(Token token) {
		return tokenRepository.save(token);
	}
	
	public Token updateToken(Token token) {
		return tokenRepository.save(token);
	}
}
