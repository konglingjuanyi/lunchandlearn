package com.pb.lunchandlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Created by de007ra on 7/14/2016.
 */
@Component
@ConfigurationProperties(prefix = "mail")
public final class MailServerSettings {
/*
	@NotNull
	private  String host;
	@NotNull
	private Integer port;
	@NotNull
	private String protocol;
	@NotNull
	private String smtpSSLTrust;
*/
	@NotNull
	private String calenderRequestSubject;

	private String emailGroup;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MailServerSettings)) return false;

		MailServerSettings that = (MailServerSettings) o;

		if (calenderRequestSubject != null ? !calenderRequestSubject.equals(that.calenderRequestSubject) : that.calenderRequestSubject != null)
			return false;
		return emailGroup != null ? emailGroup.equals(that.emailGroup) : that.emailGroup == null;

	}

	@Override
	public int hashCode() {
		int result = calenderRequestSubject != null ? calenderRequestSubject.hashCode() : 0;
		result = 31 * result + (emailGroup != null ? emailGroup.hashCode() : 0);
		return result;
	}

	public String getEmailGroup() {
		return emailGroup;
	}

	public void setEmailGroup(String emailGroup) {
		this.emailGroup = emailGroup;
	}

	public String getCalenderRequestSubject() {
		return calenderRequestSubject;
	}

	public void setCalenderRequestSubject(String calenderRequestSubject) {
		this.calenderRequestSubject = calenderRequestSubject;
	}

/*	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getSmtpSSLTrust() {
		return smtpSSLTrust;
	}

	public void setSmtpSSLTrust(String smtpSSLTrust) {
		this.smtpSSLTrust = smtpSSLTrust;
	}*/
}
