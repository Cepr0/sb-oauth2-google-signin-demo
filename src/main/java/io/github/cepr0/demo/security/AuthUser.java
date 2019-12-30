package io.github.cepr0.demo.security;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class AuthUser extends User {

	private final long id;
	private final String name;
	private final String email;
	private final String avatarUrl;

	public AuthUser(long id, String name, String email, String avatarUrl, String password) {
		super(email, password, List.of(new SimpleGrantedAuthority("USER")));
		this.id = id;
		this.name = name;
		this.email = email;
		this.avatarUrl = avatarUrl;
	}
}
