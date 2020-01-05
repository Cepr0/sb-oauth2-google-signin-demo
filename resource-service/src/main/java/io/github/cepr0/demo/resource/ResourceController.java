package io.github.cepr0.demo.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
public class ResourceController {

	private final ResourceRepo resourceRepo;

	public ResourceController(ResourceRepo resourceRepo) {
		this.resourceRepo = resourceRepo;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Resource> get(@PathVariable int id) {
		return ResponseEntity.of(resourceRepo.findById(id));
	}
}
