package io.github.cepr0.demo.security;

import lombok.NonNull;

import java.util.Map;

import static java.util.Optional.ofNullable;

public class ClaimsMapper {

	private static final String USER_ID = "user_id";
	private static final String USER_NAME = "user_name";
	private static final String USER_EMAIL = "user_email";
	private static final String USER_AVATAR = "user_avatar";

	Map<String, Object> toClaims(CustomUserDetails customUserDetails) {
		AuthUser authUser = customUserDetails.getAuthUser();
		return Map.of(
				USER_ID, authUser.getId(),
				USER_NAME, authUser.getName(),
				USER_EMAIL, authUser.getEmail(),
				USER_AVATAR, ofNullable(authUser.getAvatarUrl()).orElse("")
		);
	}

	AuthUser toAuthUser(@NonNull Map<String, ?> claims) {
		return new AuthUser(
				(Integer) claims.get(USER_ID),
				(String) claims.get(USER_NAME),
				(String) claims.get(USER_EMAIL),
				(String) claims.get(USER_AVATAR)
		);
	}
}
