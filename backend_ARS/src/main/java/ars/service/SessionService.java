package ars.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Session;

public interface SessionService {
	List<Session> findAll();
	
	Page<Session> findAll(Pageable page);
	
	Session createSession(Session session, Integer providerId);
}
