package com.pb.lunchandlearn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Created by DE007RA on 7/5/2016.
 */
@Configuration
@EnableGlobalAuthentication
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MongoDBAuthenticationProvider authenticationProvider;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/lib/**", "/html/**", "/js*/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//https://dzone.com/articles/login-page-angular-js-and
		//https://spring.io/blog/2015/01/12/the-login-page-angular-js-and-spring-security-part-ii
		http.formLogin().and().logout().permitAll().and()
			.authorizeRequests()
				.antMatchers("/lib/**", "/html/**", "/js/**", "/", "/css/**").permitAll()
				.anyRequest().authenticated()
			.and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
		.csrf().csrfTokenRepository(csrfTokenRepository());
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	public static SecuredUser getLoggedInUser() {
		if(SecurityContextHolder.getContext().getAuthentication() == null) {
			return null;
		}
		return (SecuredUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
	}
}