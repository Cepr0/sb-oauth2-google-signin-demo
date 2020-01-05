package io.github.cepr0.demo.auth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

@Getter
@Setter
@Validated
@ConfigurationProperties("demo.client")
public class ClientProps {

	/**
	 * Client application ID
	 */
	@NotBlank private String id;

	/**
	 * Client application secret
	 */
	@NotBlank private String secret;

	/**
	 * Time to live duration of Access Token, defaults to 1 hour
	 */
	private Duration accessTokenValidity = Duration.ofHours(1);

	/**
	 * Time to live duration of Refresh Token, defaults to 24 hours
	 */
	private Duration refreshTokenValidity = Duration.ofHours(24);
}
