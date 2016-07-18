package com.pb.lunchandlearn.domain;

import org.springframework.util.StringUtils;

/**
 * Created by de007ra on 5/2/2016.
 */
public enum UserRole {
	ADMIN("ADMIN"),
	MANAGER("MANAGER");

	private String role;
	private UserRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return StringUtils.capitalize(role);
	}
}
