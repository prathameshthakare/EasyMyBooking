package com.learn.authserviceforproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Optional: specify a test profile if needed
public class AuthserviceforprojectApplicationTests {

	@Test
	public void contextLoads() {
		// This test will simply check if the application context loads successfully
	}
}
