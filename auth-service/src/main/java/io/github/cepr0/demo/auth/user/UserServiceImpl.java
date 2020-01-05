package io.github.cepr0.demo.auth.user;

import io.github.cepr0.demo.auth.google.GoogleData;
import io.github.cepr0.demo.auth.google.GoogleDataProvider;
import io.github.cepr0.demo.auth.security.CustomUserDetails;
import io.github.cepr0.demo.auth.security.OAuth2AccessTokenGenerator;
import io.github.cepr0.demo.auth.user.dto.GoogleRequest;
import io.github.cepr0.demo.auth.user.dto.SignUpRequest;
import io.github.cepr0.demo.auth.user.dto.TokenDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final GoogleDataProvider google;
	private final OAuth2AccessTokenGenerator tokenGenerator;

	public UserServiceImpl(
			UserRepo userRepo,
			PasswordEncoder passwordEncoder,
			GoogleDataProvider google,
			OAuth2AccessTokenGenerator tokenGenerator
	) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.google = google;
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	public int create(@NonNull SignUpRequest request) {
		try {
			return userRepo.saveAndFlush(new User()
					.setName(request.getName())
					.setEmail(request.getEmail())
					.setPassword(passwordEncoder.encode(request.getPassword()))
			).getId();
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Such email is already exists");
		}
	}

	@Override
	public TokenDto googleToToken(@NonNull GoogleRequest request) {
		String googleToken = request.getGoogleToken();
		String clientId = request.getClientId();

		GoogleData googleData = google.getData(googleToken);

		User user = userRepo.findByEmail(googleData.getEmail())
				// update an existing user with google Id and avatar URL
				.map(target -> target
						.setGoogleId(googleData.getGoogleId())
						.setAvatarUrl(googleData.getAvatarUrl())
				)
				// or create a new one
				.orElseGet(() -> userRepo.save(new User()
						.setName(googleData.getName())
						.setEmail(googleData.getEmail())
						.setGoogleId(googleData.getGoogleId())
						.setAvatarUrl(googleData.getAvatarUrl())
				));

		UserDetails userDetails = new CustomUserDetails(user);

		try {
			return new TokenDto(user.getId(), tokenGenerator.generate(userDetails, clientId));
		} catch (ClientRegistrationException e) {
			String message = "Client with provided id is not found or incorrect";
			log.error("[!] {}: '{}'", message, clientId);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}
	}
}
