package io.github.cepr0.demo.auth.security;

import io.github.cepr0.demo.auth.user.User;
import io.github.cepr0.demo.common.AuthUser;
import lombok.Getter;

import java.util.List;

@Getter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
	private final AuthUser authUser;

	public CustomUserDetails(User user) {
		super(user.getEmail(), user.getPassword(), List.of(user.getRole()));
		this.authUser = new AuthUser(user.getId(), user.getName(), user.getEmail(), user.getAvatarUrl());
	}
}
