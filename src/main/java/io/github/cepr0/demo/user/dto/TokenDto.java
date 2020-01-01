package io.github.cepr0.demo.user.dto;

import lombok.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

@Value
public class TokenDto {
	private int userId;
	private OAuth2AccessToken token;
}
