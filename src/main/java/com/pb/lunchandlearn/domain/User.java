package com.pb.lunchandlearn.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by DE007RA on 7/5/2016.
 */
public class User {
	@Size(min = 4, max = 20)
	@NotNull
	@Indexed
	@TextIndexed
	private String name;

	@Size(min = 4, max = 20)
	@NotNull
	@Indexed
	@TextIndexed
	private String lowername;

	@NotNull
	@Email
	@Size(min = 7, max = 26)
	@Indexed
	@TextIndexed
	private String emailId;

	private List<String> roles;

	public User(String name, String emailId, List<String> roles) {
		this.name = name;
		this.emailId = emailId;
		this.roles = roles;
	}

	public User(){};

	public User(String name, String emailId) {
		this.name = name;
		this.emailId = emailId;
	}

	public void setLowername() {
		this.lowername = StringUtils.lowerCase(name);
	}

	@Override
	public String toString() {
		return "User{" +
				", name='" + name + '\'' +
				", emailId='" + emailId + '\'' +
				", roles=" + roles +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;

		User user = (User) o;

		if (name != null ? !name.equals(user.name) : user.name != null) return false;
		if (emailId != null ? !emailId.equals(user.emailId) : user.emailId != null) return false;
		return roles != null ? roles.equals(user.roles) : user.roles == null;

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (emailId != null ? emailId.hashCode() : 0);
		result = 31 * result + (roles != null ? roles.hashCode() : 0);
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}