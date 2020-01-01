package io.github.cepr0.demo.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.lang.String.format;

@Slf4j
@Component
public class GoogleDataProvider {
	
	private final GoogleIdTokenVerifier verifier;

	public GoogleDataProvider(GoogleIdTokenVerifier googleIdTokenVerifier) {
		this.verifier = googleIdTokenVerifier;
	}

	public GoogleData getData(@NonNull String googleToken) {
		try {
			GoogleIdToken idToken = verifier.verify(googleToken);
			if (idToken != null) {
				GoogleIdToken.Payload payload = idToken.getPayload();
				String googleId = payload.getSubject();
				
				if (!StringUtils.hasText(googleId)) {
					throw logAndThrowException("Google user ID is null or empty!");
				}
				
				log.debug("[d] Obtained google ID: {}", googleId);
				
				String email = payload.getEmail();
				boolean emailVerified = payload.getEmailVerified();
				String name = (String) payload.get("name");
				String pictureUrl = (String) payload.get("picture");
				String locale = (String) payload.get("locale");
				
				if (!StringUtils.hasText(email)) {
					throw logAndThrowException("Email in Google data is null or empty!");
				}
				
				if (!StringUtils.hasText(name)) {
					throw logAndThrowException("The name in Google data is null or empty!");
				}
				
				return new GoogleData(googleId, email, emailVerified, name, pictureUrl, locale);
			} else {
				throw logAndThrowException(format("Google idToken is null for token '%s'", googleToken));
			}
			
		} catch (GeneralSecurityException | IOException e) {
			throw logAndThrowException(format("Couldn't verify Google token '%s'. Cause: %s", googleToken, e.toString()));
		}
	}

	private ResponseStatusException logAndThrowException(String message) {
		log.error("[!] {}", message);
		return new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, message);
	}
}
