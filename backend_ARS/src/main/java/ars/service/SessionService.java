package ars.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Session;
import ars.exceptions.NotAllowedException;
import ars.exceptions.TimeConflictException;

public interface SessionService {
	/**
	 * Returns list of the all Sessions
	 * 
	 * @param futureOnly
	 * @return
	 */
	List<Session> findAll(boolean futureOnly);

	/**
	 * Returns paged list of the all Sessions
	 * 
	 * @param page
	 * @param futureOnly
	 * @return
	 */
	Page<Session> findAll(Pageable page, boolean futureOnly);

	/**
	 * Creates a Session for the person/provider. if the person is not a Provider
	 * then throws an NotAllowedException if session time conflicts with other
	 * session then throws an TimeConflictException
	 * 
	 * @param session
	 * @param providerId
	 * @return
	 */
	Session createSession(Session session, Integer personId) throws TimeConflictException, NotAllowedException;

}
