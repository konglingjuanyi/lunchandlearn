package com.pb.lunchandlearn.model;

import java.util.List;

/**
 * Created by DE007RA on 7/28/2015.
 */
public class Employee {
	private Long id;
	private String guid;
	private String name;
	private String emailId;
	private Gender gender;

	@Override
	public String toString() {
		return "Employee [id=" + id + ", guid=" + guid + ", name=" + name
				+ ", emailId=" + emailId + ", gender=" + gender + ", roles="
				+ roles + ", reportsTo=" + reportsTo + "]";
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	private List<String> roles;
	private String reportsTo;

	public Employee(String guid, String name, String emailId,
			List<String> roles, String reportsTo) {
		this.guid = guid;
		this.name = name;
		this.emailId = emailId;
		this.roles = roles;
		this.reportsTo = reportsTo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee(Long id, String name, String emailId, Gender gender) {
		this.id = id;
		this.name = name;
		this.emailId = emailId;
		this.gender = gender;
	}

	public Employee() {
	}

	public String getGuid() {
		return guid;
	}

	public String getName() {
		return name;
	}

	public String getEmailId() {
		return emailId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getReportsTo() {
		return reportsTo;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public static enum Gender {
		MALE,
		FEMALE;
	}
}
