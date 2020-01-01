package io.github.cepr0.demo.user;

import io.github.cepr0.demo.security.AuthUser;
import io.github.cepr0.demo.user.dto.GoogleRequest;
import io.github.cepr0.demo.user.dto.SignUpRequest;
import io.github.cepr0.demo.user.dto.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public AuthUser demo(OAuth2Authentication auth) {
		var details = (OAuth2AuthenticationDetails) auth.getDetails();
		return (AuthUser) details.getDecodedDetails();
	}

	@PostMapping("/me/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
		int userId = userService.create(request);
		return ResponseEntity.created(URI.create("/users/" + userId)).build();
	}

	@PostMapping("/me/google")
	public ResponseEntity<TokenDto> signup(@Valid @RequestBody GoogleRequest request) {
		TokenDto result = userService.googleToToken(request);
		return ResponseEntity.ok(result);
	}
}