package io.github.cepr0.demo.auth.user.dto;


import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class GoogleRequest {
	@NotBlank private String googleToken;
	@NotBlank private String clientId;
}
