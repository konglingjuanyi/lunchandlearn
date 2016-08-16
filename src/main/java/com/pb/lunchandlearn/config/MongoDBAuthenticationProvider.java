package com.pb.lunchandlearn.config;

import com.pb.lunchandlearn.domain.Employee;
import com.pb.lunchandlearn.domain.UserRole;
import com.pb.lunchandlearn.repository.EmployeeRepository;
import com.pb.lunchandlearn.service.LDAPService;
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
import java.util.List;

/**
 * Created by DE007RA on 7/5/2016.
 */
@Service
public class MongoDBAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private EmployeeRepository employeeRepository;

	private static List<GrantedAuthority> empAuthorities = new ArrayList<>(1);
	@Autowired
	private LDAPService ldapService;

	static {
		empAuthorities.add(new SimpleGrantedAuthority(UserRole.EMPLOYEE.toString()));
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	@Override
	protected UserDetails retrieveUser(String guid, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		UserDetails userDetails;

		//ldap authentication
		if (StringUtils.isEmpty(guid) || StringUtils.isEmpty(authentication.getCredentials())) {
			throw new UsernameNotFoundException("");
		}
		guid = guid.toUpperCase();
		try {
//			ldapService.authenticateEmployee(guid, authentication.getCredentials().toString());
			Employee emp = employeeRepository.findByGuid(guid);
			if (emp == null) {
				//first time user fetch the information and save it
				emp = ldapService.getEmployee(guid);
				if(emp == null) {
					emp = ldapService.addEmployee(guid);
					emp = employeeRepository.insert(emp);
				}
				if(emp == null) {
					throw new UsernameNotFoundException(MessageFormat.format("User Name: {0}", guid));
				}
			}
			userDetails = new SecuredUser(emp.getGuid(), emp.getName(), authentication.getCredentials().toString(),
					emp.getEmailId(), getAuthorities(emp.getRoles()));
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
			List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			return authorities;
		}
		return empAuthorities;
	}
}