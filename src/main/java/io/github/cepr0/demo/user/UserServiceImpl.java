package io.github.cepr0.demo.user;

import io.github.cepr0.demo.google.GoogleData;
import io.github.cepr0.demo.google.GoogleDataProvider;
import io.github.cepr0.demo.security.CustomUserDetails;
import io.github.cepr0.demo.user.dto.GoogleRequest;
import io.github.cepr0.demo.user.dto.SignUpRequest;
import io.github.cepr0.demo.user.dto.TokenDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static java.util.Collections.singletonList;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

	private static final String PASSWORD_GRANT_TYPE = "password";
	private static final String SCOPE = "*";

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final GoogleDataProvider google;
	private final ClientDetailsService clientDetailsService;
	private final AuthorizationServerTokenServices tokenServices;

	public UserServiceImpl(
			UserRepo userRepo,
			PasswordEncoder passwordEncoder,
			GoogleDataProvider google,
			ClientDetailsService clientDetailsService,
			AuthorizationServerEndpointsConfiguration endpoints
	) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.google = google;
		this.clientDetailsService = clientDetailsService;

		var configurer = endpoints.getEndpointsConfigurer();
		this.tokenServices = configurer.getTokenServices();
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
				.map(target -> target
						.setGoogleId(googleData.getGoogleId())
						.setAvatarUrl(googleData.getAvatarUrl())
				)
				.orElseGet(() -> userRepo.save(new User()
						.setName(googleData.getName())
						.setEmail(googleData.getEmail())
						.setGoogleId(googleData.getGoogleId())
						.setAvatarUrl(googleData.getAvatarUrl())
				));

		return getAccessToken(user, clientId);
	}

	private TokenDto getAccessToken(User user, String clientId) {

		UserDetails userDetails = new CustomUserDetails(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getAvatarUrl(),
				""
		);

		ClientDetails client;
		try {
			client = clientDetailsService.loadClientByClientId(clientId);
		} catch (ClientRegistrationException e) {
			log.error("[!] Couldn't find a client data by clientId '{}", clientId);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided client id is incorrect");
		}

		var authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails, userDetails.getAuthorities());

		Map<String, String> params = Map.of(
				"grant_type", PASSWORD_GRANT_TYPE,
				"scope", SCOPE
		);

		var tokenRequest = new TokenRequest(params, clientId, singletonList(SCOPE), PASSWORD_GRANT_TYPE);
		var oAuth = new OAuth2Authentication(tokenRequest.createOAuth2Request(client), authToken);
		var accessToken = tokenServices.createAccessToken(oAuth);
		return new TokenDto(user.getId(), accessToken);
	}
}
