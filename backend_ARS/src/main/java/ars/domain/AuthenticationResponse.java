package ars.domain;

public class AuthenticationResponse {

	private final String jwt;
	
	
	public AuthenticationResponse(String jwt) {
		this.jwt = jwt;
		// TODO Auto-generated constructor stub
	}

	public String getJwt() {
		return jwt;
	}
	
	

}
