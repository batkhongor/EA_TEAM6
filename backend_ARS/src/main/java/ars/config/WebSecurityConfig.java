package ars.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ars.domain.RoleType;
import ars.filter.JwtRequestFilter;
import ars.service.impl.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
     
	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    	
    	//  to get userdata from database using userDetailServiceImpl
    	auth.userDetailsService(userDetailServiceImpl);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	PasswordEncoder encoder = new BCryptPasswordEncoder();
    	return encoder;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
		http		
		.authorizeRequests()
			.antMatchers("/favicon.ico").permitAll()
			.antMatchers("/authenticate").permitAll()
			//.antMatchers("/hello").permitAll()
			.antMatchers("/provider/*").permitAll()
			.antMatchers("/logout").authenticated()
			//.antMatchers("/student").authenticated()
			//.antMatchers("/client/**").hasAnyAuthority(RoleType.ADMIN.toString(), RoleType.CUSTOMER.toString())
			.antMatchers("/admin/**").hasAnyAuthority(RoleType.ADMIN.toString())
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().csrf().disable()
			
			/*
			.formLogin()*/
			;
			
			http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
    }
    
    
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
    	// TODO Auto-generated method stub
    	return super.authenticationManager();
    }
    
}

