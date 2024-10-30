package com.example.vidupcoremodule.core.entity;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Role;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Represents an Authenticated User
 */
@Data
public class LoggedInUserDetails implements UserDetails, Principal {

	private final User user;
	
	public LoggedInUserDetails(User user) {
		this.user = user;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	private String sessionToken;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		
		return authorities;
	}

	public Set<Role> getRoles()
	{
		return user.getRoles();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}



	public Integer getUserId(){return user.getId();}

	public Set<Role> getUserRoles(){return user.getRoles();}

	public User getUser(){return user;}

	@Override
	public boolean isAccountNonExpired() {
		return user != null;
	}

	@Override
	public boolean isAccountNonLocked() {
		return user != null;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return user != null;
	}

	@Override
	public boolean isEnabled() {

        return user != null;
    }


	@Override
	public String getName() {
		if(user!=null)
			return user.getUserName();
		return "";
	}

	@Override
	public boolean implies(Subject subject) {
		return Principal.super.implies(subject);
	}
}
