package com.pb.lunchandlearn.config;

import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DE007RA on 7/5/2016.
 */
@Service
public class MongoDBAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		UserDetails userDetails = null;

		if(StringUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("User Name: ");
		}
		try {
			Employee emp = employeeRepository.findByGuid(username.toUpperCase());
			if(emp == null) {
				throw new UsernameNotFoundException(MessageFormat.format("User Name: {0}", username));
			}
			userDetails = new SecuredUser(emp.getGuid(), emp.getName(), emp.getGuid(), emp.getEmailId(),
					getAuthorities(emp.getRoles()));
		} catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		if (userDetails == null) {
			throw new InternalAuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}
		return userDetails;
	}

	private List<GrantedAuthority> getAuthorities(List<String> roles) {
		if (!CollectionUtils.isEmpty(roles)) {
			List<GrantedAuthority> authorities = new ArrayList<>(3);
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			return authorities;
		}
		return Collections.EMPTY_LIST;
	}
}