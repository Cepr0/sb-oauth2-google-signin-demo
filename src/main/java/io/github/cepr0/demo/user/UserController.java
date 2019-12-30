package io.github.cepr0.demo.user;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

	@GetMapping("/me")
	public Map<String, ?> demo(OAuth2Authentication auth) {

		var details = (OAuth2AuthenticationDetails) auth.getDetails();
		//noinspection unchecked
		var decodedDetails = (Map<String, Object>) details.getDecodedDetails();

		return Map.of(
				"id", decodedDetails.get("user_id"),
				"name", decodedDetails.get("user_name"),
				"email", decodedDetails.get("user_email"),
				"avatar", decodedDetails.get("user_avatar"),
				"roles", decodedDetails.get("authorities")
		);
	}
}