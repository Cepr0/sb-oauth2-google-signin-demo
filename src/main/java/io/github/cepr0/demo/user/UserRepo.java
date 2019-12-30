package io.github.cepr0.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
