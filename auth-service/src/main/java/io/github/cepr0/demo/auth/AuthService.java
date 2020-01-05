package io.github.cepr0.demo.auth;

import org.h2.tools.Server;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

@SpringBootApplication(scanBasePackages = {
		"io.github.cepr0.demo.auth",
		"io.github.cepr0.demo.common"
})
public class AuthService {
	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(AuthService.class)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
	}
}
