package io.github.cepr0.demo.user;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoData {

	private final UserRepo userRepo;

	public DemoData(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void populateCustomers() {
		userRepo.saveAll(List.of(
			new User().setName("User1").setEmail("user1@mail.com").setPassword("{noop}123456"),
			new User().setName("User2").setEmail("user2@mail.com").setPassword("{noop}123456"),
			new User().setName("User3").setEmail("user3@mail.com").setPassword("{noop}123456")
		));
	}
}
