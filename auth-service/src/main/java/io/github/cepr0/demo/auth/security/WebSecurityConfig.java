package io.github.cepr0.demo.auth.security;


import io.github.cepr0.demo.auth.user.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserRepo userRepo;

	public WebSecurityConfig(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Transactional(readOnly = true)
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return email -> userRepo.findByEmail(email)
				.map(CustomUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User with the given email not found!"));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}

