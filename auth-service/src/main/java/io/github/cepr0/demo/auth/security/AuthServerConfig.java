package io.github.cepr0.demo.auth.security;

import io.github.cepr0.demo.common.AuthUser;
import io.github.cepr0.demo.common.ClaimsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(ClientProps.class)
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	private final AuthenticationManager authenticationManager;
	private final ClientProps clientProps;
	private final ClaimsMapper claimsMapper;
	private final JwtAccessTokenConverter tokenConverter;
	private final TokenStore tokenStore;

	@Override
	public void configure(ClientDetailsServiceConfigurer clientDetailsService) throws Exception {
		clientDetailsService.inMemory()
				.withClient(clientProps.getId())
				.secret(clientProps.getSecret())
				.scopes("*")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds((int) clientProps.getAccessTokenValidity().getSeconds())
				.refreshTokenValiditySeconds((int) clientProps.getRefreshTokenValidity().getSeconds());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain chain = new TokenEnhancerChain();
		chain.setTokenEnhancers(List.of(this::enhanceToken, tokenConverter));
		endpoints.authenticationManager(authenticationManager)
				.tokenStore(tokenStore)
				.reuseRefreshTokens(false)
				.tokenEnhancer(chain);
	}

	private OAuth2AccessToken enhanceToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			if (accessToken instanceof DefaultOAuth2AccessToken) {
				AuthUser authUser = customUserDetails.getAuthUser();
				((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(claimsMapper.toClaims(authUser));
			}
		}
		return accessToken;
	}
}


