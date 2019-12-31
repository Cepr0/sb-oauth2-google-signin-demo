package io.github.cepr0.demo.security;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class CustomUserDetails extends User {
	private final AuthUser authUser;

	public CustomUserDetails(long id, String name, String email, String avatarUrl, String password) {
		super(email, password, List.of(new SimpleGrantedAuthority("USER")));
		this.authUser = new AuthUser(id, name, email, avatarUrl);
	}
}
