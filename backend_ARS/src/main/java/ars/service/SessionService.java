package ars.service;

import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ars.domain.Session;
import ars.exceptions.NotAllowedException;
import ars.exceptions.NotFoundException;
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
	 * Returns list of the all Sessions by its Provider
	 * 
	 * @param futureOnly
	 * @return
	 */
	List<Session> findAllByEmail(String email, boolean futureOnly);

	/**
	 * Gets single session
	 * 
	 * @param sessionId
	 * @return
	 */
	default Session getSession(Integer sessionId) throws NotFoundException {
		throw new NotYetImplementedException();
	}

	/**
	 * Creates a Session for the person/provider. if the person is not a Provider
	 * then throws an NotAllowedException if session time conflicts with other
	 * session then throws an TimeConflictException
	 * 
	 * @param session
	 * @param providerEMail
	 * @return
	 */
	Session createSession(Session session, String providerEMail) throws TimeConflictException, NotAllowedException;

	/**
	 * Update a Session for the person/provider. if the person is not a Provider
	 * then throws an NotAllowedException if session time conflicts with other
	 * session then throws an TimeConflictException
	 * 
	 * @param session
	 * @param providerEMail
	 * @return
	 */
	Session updateSession(Integer sessionId, Session session, String providerEMail)
			throws TimeConflictException, NotAllowedException, NotFoundException;

	/**
	 * Deletes a Session of the given provider. if the person is not a Provider then
	 * throws an NotAllowedException if session time conflicts with other
	 * 
	 * @param session
	 * @param providerEMail
	 * @return
	 */
	void deleteSession(Integer sessionId, String providerEMail) throws NotAllowedException, NotFoundException;

	/**
	 * Deletes a Session. if the person is not a Provider then throws an
	 * NotAllowedException if session time conflicts with other
	 * 
	 * @param session
	 * @param providerId
	 * @return
	 */
	void deleteSession(Integer sessionId);

}
