package com.spring.training;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

	@Container
	static MySQLContainer mysql = new MySQLContainer("mysql:5.7")
				.withDatabaseName("spring_training")
				.withUsername("user")
				.withPassword("password");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> mysql.getJdbcUrl() + "?enabledTLSProtocols=TLSv1.2");
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);

	}

	@Test
	public void contextLoads() {
	}

}
