package ars.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ars.domain.Person;
import ars.domain.Token;
import ars.dto.AuthenticationRequest;
import ars.dto.AuthenticationResponse;
import ars.service.PersonService;
import ars.service.TokenService;
import ars.service.impl.PersonServiceImpl;
import ars.service.impl.TokenServiceImpl;
import ars.service.impl.UserDetailServiceImpl;
import ars.utils.JwtUtils;

@RestController
public class SecurityController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;

	@Autowired
	private PersonServiceImpl personServiceImpl;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private TokenServiceImpl tokenServiceImpl;

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<?> logout() {

		// invalidate token on database when user logout successfully

		String jwt_token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		Token token = tokenServiceImpl.findTokenById(jwt_token).get();
		token.setValid();

		tokenServiceImpl.updateToken(token);
		SecurityContextHolder.getContext().setAuthentication(null);

		return ResponseEntity.ok("logged out");
	}

	@RequestMapping(value = "/logout/{personId}", method = RequestMethod.POST)
	public ResponseEntity<?> logoutAll() {
		// logout from all devices that user logged in
		// invalidate all token related to user on database when user logout
		// successfully

		String jwt_token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		Integer personId = personServiceImpl.findByEmail(email).get().getId();

		List<Token> tokens = tokenServiceImpl.findAllTokensByPerson(personId);

		for (Token token : tokens) {
			token.setValid();
		}
		tokenServiceImpl.updateAllToken(tokens);
		SecurityContextHolder.getContext().setAuthentication(null);

		return ResponseEntity.ok("logged out from all devices");
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest,
			HttpServletResponse response) throws Exception {
		
		System.out.println("THis is authenticate");
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
				authenticationRequest.getPassword()));

		final UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtUtils.generateToken(userDetails);

		// saved token on database when user login successfully
		// late we use it to logout or invalidate jwt token
		Person person = personServiceImpl.findByEmail(userDetails.getUsername()).get();
		Token jwt_token = new Token(jwt, person);
		tokenServiceImpl.createToken(jwt_token);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
