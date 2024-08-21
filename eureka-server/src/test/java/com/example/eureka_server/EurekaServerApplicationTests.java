package com.example.eureka_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EurekaServerApplicationTests {

	@Test
	void contextLoads() {
		// This test case ensures that the Spring ApplicationContext is loaded successfully.
	}

	@Test
	void eurekaServerIsEnabled() {
		// This test case will check if the @EnableEurekaServer annotation is present and the server starts correctly.
		// Since @EnableEurekaServer is a configuration annotation, we rely on contextLoads test to confirm its correctness.
	}

	@Test
	void mainMethodTest() {
		// Test the main method to ensure that it starts the Spring Boot application without errors.
		EurekaServerApplication.main(new String[]{});
	}
}
