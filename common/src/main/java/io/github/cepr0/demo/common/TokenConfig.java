package io.github.cepr0.demo.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(TokenProps.class)
public class TokenConfig {

	private final TokenProps tokenProps;
	private final ClaimsMapper claimsMapper;

	public TokenConfig(TokenProps tokenProps, ClaimsMapper claimsMapper) {
		this.tokenProps = tokenProps;
		this.claimsMapper = claimsMapper;
	}

	@Bean
	public JwtAccessTokenConverter tokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(tokenProps.getSigningKey());
		converter.setAccessTokenConverter(new DefaultAccessTokenConverter() {
			@Override
			public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
				OAuth2Authentication authentication = super.extractAuthentication(claims);
				authentication.setDetails(claimsMapper.toAuthUser(claims));
				return authentication;
			}
		});
		return converter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(tokenConverter());
	}
}
