package com.pb.lunchandlearn.config;

import com.pb.lunchandlearn.domain.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Created by de007ra on 7/7/2016.
 */
public final class SecuredUser implements UserDetails {

	private static GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority(UserRole.ADMIN.name());
	private static GrantedAuthority MANAGER_AUTHORITY = new SimpleGrantedAuthority(UserRole.MANAGER.name());

	private static final long serialVersionUID = -6174534965909137051L;
	private String guid;
	private String userName;
	private String pwd;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	public SecuredUser(String guid, String username, String password, String emailId,
					   Collection<? extends GrantedAuthority> authorities) {
		this.guid = guid.toUpperCase();
		this.emailId = emailId;
		this.authorities = authorities;
		this.userName = username;
		this.pwd = password;
	}

	public String getGuid() {
		return guid.toUpperCase();
	}

	public void setGuid(String guid) {
		this.guid = guid.toUpperCase();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.pwd;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	private String emailId;

	public boolean isAdmin() {
		return this.getAuthorities().contains(ADMIN_AUTHORITY);
	}

	public boolean isManager() {
		return this.getAuthorities().contains(MANAGER_AUTHORITY);
	}

}