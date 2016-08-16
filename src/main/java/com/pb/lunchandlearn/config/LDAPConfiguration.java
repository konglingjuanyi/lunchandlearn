package com.pb.lunchandlearn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * Created by DE007RA on 8/4/2016.
 */
@Configuration
public class LDAPConfiguration {

	@Autowired
	private LDAPSettings ldapSettings;

	@Bean
	public LdapTemplate ldapTemplate() {
		LdapContextSource contextSource = getContextSource();
		return new LdapTemplate(contextSource);
	}

	private LdapContextSource getContextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUserDn(ldapSettings.getServiceDN());
		contextSource.setPassword(ldapSettings.getServicePwd());
		contextSource.setUrl(ldapSettings.getServerUrl());
		contextSource.afterPropertiesSet();
		return contextSource;
	}
}