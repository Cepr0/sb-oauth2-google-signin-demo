package io.github.cepr0.demo.user;

import io.github.cepr0.demo.user.dto.SignUpRequest;
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
}
