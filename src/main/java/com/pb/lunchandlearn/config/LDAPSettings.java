package com.pb.lunchandlearn.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by DE007RA on 8/5/2016.
 */
@Component
@ConfigurationProperties(prefix = "ldap")
public final class LDAPSettings {
	private String baseDNs;
	private String serviceDN;
	private String servicePwd;
	private String serverUrl;
	private String[] arrayBaseDNs;

	public String getBaseDNs() {
		return baseDNs;
	}

	public void setBaseDNs(String baseDNs) {
		this.baseDNs = baseDNs;
	}

	public void setServiceDN(String serviceDN) {
		this.serviceDN = serviceDN;
	}

	public void setServicePwd(String servicePwd) {
		this.servicePwd = servicePwd;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public void setArrayBaseDNs(String[] arrayBaseDNs) {
		this.arrayBaseDNs = arrayBaseDNs;
	}

	public String getServiceDN() {
		return serviceDN;
	}

	public String getServicePwd() {
		return servicePwd;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	@PostConstruct
	public void init() {
		if (!StringUtils.isEmpty(baseDNs)) {
			arrayBaseDNs = baseDNs.split("; *");
		}
	}

	public String[] getArrayBaseDNs() {
		return arrayBaseDNs;
	}
}