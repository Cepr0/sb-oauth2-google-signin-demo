package io.github.cepr0.demo.user;

import io.github.cepr0.demo.user.dto.SignUpRequest;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
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
}
