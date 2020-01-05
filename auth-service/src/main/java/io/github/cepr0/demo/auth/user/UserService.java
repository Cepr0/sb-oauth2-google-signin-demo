package io.github.cepr0.demo.auth.user;

import io.github.cepr0.demo.auth.user.dto.GoogleRequest;
import io.github.cepr0.demo.auth.user.dto.SignUpRequest;
import io.github.cepr0.demo.auth.user.dto.TokenDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface UserService {
	/**
	 * Create a new {@link User}. If user email is already exists,
	 * throws {@link ResponseStatusException} with {@link HttpStatus#BAD_REQUEST} status code.
	 *
	 * @param request {@link SignUpRequest} must not be null
	 * @return id of a new {@link User}
	 */
	int create(SignUpRequest request);

	/**
	 * Takes Google token, verifies it, retrieves Google user data, verifies if such a user is already exists in database,
	 * if exists, creates access and refresh tokens.
	 * Otherwise first creates a new user and then creates access and refresh tokens.
	 *
	 * @param request {@link GoogleRequest} with Google token
	 * @return {@link TokenDto} with access and refresh tokens
	 */
	TokenDto googleToToken(GoogleRequest request);
}
