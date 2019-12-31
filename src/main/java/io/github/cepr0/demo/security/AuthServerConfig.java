package io.github.cepr0.demo.security;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Configuration
@EnableConfigurationProperties(AuthProps.class)
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	private final AuthenticationManager authenticationManager;
	private final AuthProps props;

	public AuthServerConfig(AuthenticationManager authenticationManager, AuthProps props) {
		this.authenticationManager = authenticationManager;
		this.props = props;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clientDetailsService) throws Exception {
		clientDetailsService.inMemory()
				.withClient("client")
				.secret("{noop}") // don't use the password for our public client
				.scopes("*")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds((int) props.getAccessTokenValidity().getSeconds())
				.refreshTokenValiditySeconds((int) props.getRefreshTokenValidity().getSeconds());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain chain = new TokenEnhancerChain();
		chain.setTokenEnhancers(List.of(
				(accessToken, authentication) -> {
					if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
						CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
						AuthUser authUser = customUserDetails.getAuthUser();
						Map<String, Object> additionalInfo = Map.of(
								"user_id", authUser.getId(),
								"user_name", authUser.getName(),
								"user_email", authUser.getEmail(),
								"user_avatar", ofNullable(authUser.getAvatarUrl()).orElse("")
						);
						((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
					}
					return accessToken;
				},
				tokenConverter()
		));

		endpoints.authenticationManager(authenticationManager)
				.tokenStore(tokenStore())
				.reuseRefreshTokens(false)
				.tokenEnhancer(chain);
	}

	@Bean
	public JwtAccessTokenConverter tokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(props.getSigningKey());

		converter.setAccessTokenConverter(new DefaultAccessTokenConverter() {
			@Override
			public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
				OAuth2Authentication authentication = super.extractAuthentication(claims);
				AuthUser authUser = new AuthUser(
						(Integer) claims.get("user_id"),
						(String) claims.get("user_name"),
						(String) claims.get("user_email"),
						(String) claims.get("user_avatar")
				);
				authentication.setDetails(authUser);
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


