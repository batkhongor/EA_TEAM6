package ars.service;

import ars.domain.Appointment;
import ars.domain.Person;
import ars.domain.RoleType;
import ars.domain.Session;
import ars.domain.Token;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface TokenService {

    //List of all tokens

    public default List<Token> findAllTokens() {
        throw new NotYetImplementedException();
    }

    public default List<Token> findAllTokensByPerson(Integer personId) {
        throw new NotYetImplementedException();
    }
    
    public default Optional<Token> findTokenById(String token)
    {
        throw new NotYetImplementedException();
    }

    public default Token createToken(Token token) {
        throw new NotYetImplementedException();
    }


    public default Token updateToken(Token token) {
        throw new NotYetImplementedException();
    }
    
    public default  List<Token> updateAllToken(List<Token> tokens) {
        throw new NotYetImplementedException();
    }
}



