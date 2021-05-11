package ars.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		List< GrantedAuthority> authorities=new ArrayList<>();
		GrantedAuthority role=new SimpleGrantedAuthority("faculty");
		authorities.add(role);
		
		User  user=new User("admin", encoder.encode("admin") , authorities);
		
		
		return user;
	}
	
}
