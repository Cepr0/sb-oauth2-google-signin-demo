package io.github.cepr0.demo.resource;

import org.h2.tools.Server;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.sql.SQLException;

@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication(
		exclude = UserDetailsServiceAutoConfiguration.class,
		scanBasePackages = {
				"io.github.cepr0.demo.resource",
				"io.github.cepr0.demo.common"
		}
)
public class ResourceService {
	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(ResourceService.class)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9093");
	}
}
