package io.github.cepr0.demo.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

@Getter
@Setter
@Validated
@ConfigurationProperties("demo.auth")
public class AuthProps {

	/**
	 * JWT signing key. It can be either a simple MAC key or an RSA key
	 */
	@NotBlank private String signingKey;

	/**
	 * Time to live duration of Access Token, defaults to 1 hour
	 */
	private Duration accessTokenValidity = Duration.ofHours(1);

	/**
	 * Time to live duration of Refresh Token, defaults to 24 hours
	 */
	private Duration refreshTokenValidity = Duration.ofHours(24);
}
