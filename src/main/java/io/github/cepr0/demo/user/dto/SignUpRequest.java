package io.github.cepr0.demo.user.dto;


import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class SignUpRequest {
	@NotBlank private String name;
	@NotBlank private String email;
	@NotBlank private String password;
}
