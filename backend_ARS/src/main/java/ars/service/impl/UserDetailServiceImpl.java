package ars.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ars.domain.Person;
import ars.domain.RoleType;
import lombok.Setter;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	private PersonServiceImpl personServiceImpl;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Person person = personServiceImpl.findByEmail(username).orElse(null);
        if (person == null) {
            throw new UsernameNotFoundException(username);
        }
		
		List< GrantedAuthority> authorities=new ArrayList<>();
		Set<RoleType> roles=person.getRoles();
		
		for(RoleType roleType : roles) {
			System.out.println(roleType.toString());
			GrantedAuthority role=new SimpleGrantedAuthority(roleType.toString());
			authorities.add(role);
		}
		
		User  user=new User(person.getEmail() , person.getPassword() , authorities);
		
		return user;
	}
	
}
