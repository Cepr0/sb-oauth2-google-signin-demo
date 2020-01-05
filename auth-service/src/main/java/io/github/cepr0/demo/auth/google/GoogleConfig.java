package io.github.cepr0.demo.auth.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration
@EnableConfigurationProperties(GoogleProps.class)
public class GoogleConfig {
	
	private final GoogleProps googleProps;
	
	public GoogleConfig(GoogleProps googleProps) {
		this.googleProps = googleProps;
	}
	
	@Bean
	public GoogleIdTokenVerifier googleIdTokenVerifier() {
		return new GoogleIdTokenVerifier
						.Builder(Utils.getDefaultTransport(), Utils.getDefaultJsonFactory())
						.setAudience(singletonList(googleProps.getClientId()))
						.build();
	}
}
