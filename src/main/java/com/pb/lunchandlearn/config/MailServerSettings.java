package com.pb.lunchandlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

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

	private String emailGroups;

	private String[] emailGrps;

	private String userEmailId;

	public String getUserEmailId() {
		return userEmailId;
	}

	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	@Override
	public String toString() {
		return "MailServerSettings{" +
				"calenderRequestSubject='" + calenderRequestSubject + '\'' +
				", emailGroups='" + emailGroups + '\'' +
				", emailGrps=" + Arrays.toString(emailGrps) +
				", userEmailId='" + userEmailId + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MailServerSettings)) return false;

		MailServerSettings that = (MailServerSettings) o;

		if (calenderRequestSubject != null ? !calenderRequestSubject.equals(that.calenderRequestSubject) : that.calenderRequestSubject != null)
			return false;
		if (emailGroups != null ? !emailGroups.equals(that.emailGroups) : that.emailGroups != null) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(emailGrps, that.emailGrps)) return false;
		return userEmailId != null ? userEmailId.equals(that.userEmailId) : that.userEmailId == null;

	}

	@Override
	public int hashCode() {
		int result = calenderRequestSubject != null ? calenderRequestSubject.hashCode() : 0;
		result = 31 * result + (emailGroups != null ? emailGroups.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(emailGrps);
		result = 31 * result + (userEmailId != null ? userEmailId.hashCode() : 0);
		return result;
	}

	@PostConstruct
	public void init() {
		emailGrps = this.emailGroups.split("; +");
	}

	public String[] getEmailGroups() {
		return emailGrps;
	}

	public void setEmailGroups(String emailGroups) {
		this.emailGroups = emailGroups;
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
