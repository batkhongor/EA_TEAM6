package ars.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ars.domain.AuthenticationRequest;
import ars.domain.AuthenticationResponse;
import ars.service.impl.UserDetailServiceImpl;
import ars.utils.JwtUtils;



/**
 * @author A
 *
 */
@RestController
// @RestController This also can be used instead of @Controller so that we don't
// have to user @ResponseBody annotation.

public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@RequestMapping("/")
	public String welcome() {
		return "Welcome to Page";
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			// TODO: handle exception
			throw new Exception("Incorrect password or username", e);
		}

		final UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtUtils.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
