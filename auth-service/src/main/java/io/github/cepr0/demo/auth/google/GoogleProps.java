package io.github.cepr0.demo.auth.google;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "demo.google")
public class GoogleProps {
	/**
	 * Google client id.
	 */
	@NotBlank private String clientId;
}
