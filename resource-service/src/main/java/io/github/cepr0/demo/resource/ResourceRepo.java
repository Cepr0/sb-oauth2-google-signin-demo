package io.github.cepr0.demo.resource;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepo extends JpaRepository<Resource, Integer> {
}
