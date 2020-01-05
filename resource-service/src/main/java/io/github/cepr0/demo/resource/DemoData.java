package io.github.cepr0.demo.resource;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoData {

	private final ResourceRepo resourceRepo;

	public DemoData(ResourceRepo resourceRepo) {
		this.resourceRepo = resourceRepo;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady() {
		resourceRepo.saveAll(List.of(
				new Resource().setName("Resource #1").setType(Resource.ValueType.HIGH_VALUE),
				new Resource().setName("Resource #2").setType(Resource.ValueType.MIDDLE_VALUE),
				new Resource().setName("Resource #3").setType(Resource.ValueType.LOW_VALUE)
		));
	}
}
