package io.github.cepr0.demo.auth.user.dto;


import io.github.cepr0.demo.auth.user.User;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class SignUpRequest {
	@NotBlank private String name;
	@NotBlank private String email;
	@NotBlank private String password;
	private User.Role role;
}
