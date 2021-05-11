package ars.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
     
	private static final String USERNAME_QUERY = "";
	
	private static final String AUTHORITIES_BY_USERNAME_QUERY = "";
 
    @Autowired
    private DataSource dataSource;
     
    @Autowired
    protected void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    	PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        auth.inMemoryAuthentication()
    		.passwordEncoder(encoder)
    		.withUser("service")
    		.password(encoder.encode("123"))
    		.roles("Service");
    	
//        auth.jdbcAuthentication()        	
//        	.passwordEncoder(encoder) 
//            .dataSource(dataSource)
//            .usersByUsernameQuery(USERNAME_QUERY)
//            .authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests().antMatchers("/").permitAll().and().csrf().disable();
    	
//		http		
//		.authorizeRequests()
//			.antMatchers("/favicon.ico").permitAll()
//			.antMatchers("/**").permitAll()
//			.antMatchers("/admin/**").permitAll();
//			.antMatchers("/countries/**").hasAnyRole("Service", "EM")
//			.antMatchers("/people/**").hasRole("Service")
//			.anyRequest().authenticated()
//			.and()
//		.formLogin();
    }
    
}

