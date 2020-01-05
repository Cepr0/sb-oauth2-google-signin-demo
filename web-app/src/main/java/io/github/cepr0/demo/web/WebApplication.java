package io.github.cepr0.demo.web;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
              .sources(WebApplication.class)
              .bannerMode(Banner.Mode.OFF)
              .run(args);
    }
}
