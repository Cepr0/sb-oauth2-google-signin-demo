package io.github.cepr0.demo.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@ConfigurationProperties("demo.token")
public class TokenProps {

	/**
	 * JWT signing key. It can be either a simple MAC key or an RSA key
	 */
	@NotBlank private String signingKey;
}
