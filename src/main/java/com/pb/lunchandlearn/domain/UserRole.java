package com.pb.lunchandlearn.domain;

import org.springframework.util.StringUtils;

/**
 * Created by de007ra on 5/2/2016.
 */
public enum UserRole {
	EMPLOYEE("EMPLOYEE"),
	ADMIN("ADMIN"),
	CLERICAL("CLERICAL"),
	MANAGER("MANAGER");

	private String role;

	UserRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return StringUtils.capitalize(role);
	}
}
