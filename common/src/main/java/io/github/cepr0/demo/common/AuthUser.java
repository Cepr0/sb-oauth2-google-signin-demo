package io.github.cepr0.demo.common;

import lombok.Value;

@Value
public class AuthUser {
	private long id;
	private String name;
	private String email;
	private String avatarUrl;
}
