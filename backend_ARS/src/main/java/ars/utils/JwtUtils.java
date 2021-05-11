package ars.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {
	
	private SecretKey SECRET_KEY;
	
	
	
	public JwtUtils() {
		this.SECRET_KEY=Keys.secretKeyFor(SignatureAlgorithm.HS256);
		System.out.println("____________________");
		System.out.println(SECRET_KEY);
		System.out.println("____________________");
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims=new HashMap<>();
		
		return createToken(claims, userDetails.getUsername());
	}
	
	public String createToken(Map<String, Object> claims, String subject) {
		
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username=extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) );
	}
}
