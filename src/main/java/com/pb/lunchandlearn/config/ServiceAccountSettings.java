package com.pb.lunchandlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by de007ra on 5/3/2016.
 */
@Component
@ConfigurationProperties(prefix = "service.account")
public final class ServiceAccountSettings {
	private String guid;
	private String pwd;
	private String emailId;
	private String emailPersonName;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmailPersonName() {
		return emailPersonName;
	}

	public void setEmailPersonName(String emailPersonName) {
		this.emailPersonName = emailPersonName;
	}

	@Override
	public String toString() {
		return "ServiceAccountSettings{" +
				"guid='" + guid + '\'' +
				", emailId='" + emailId + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ServiceAccountSettings)) return false;

		ServiceAccountSettings that = (ServiceAccountSettings) o;

		if (guid != null ? !guid.equals(that.guid) : that.guid != null) return false;
		return emailId != null ? emailId.equals(that.emailId) : that.emailId == null;

	}

	@Override
	public int hashCode() {
		int result = guid != null ? guid.hashCode() : 0;
		result = 31 * result + (emailId != null ? emailId.hashCode() : 0);
		return result;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}
