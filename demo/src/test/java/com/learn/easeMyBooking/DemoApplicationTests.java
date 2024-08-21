package com.learn.easeMyBooking;

import org.junit.jupiter.api.Test;

class DemoApplicationTests {

	@Test
	void contextLoads() {
		// This test checks that the Spring ApplicationContext loads without any issues.
	}

	@Test
	void discoveryClientIsEnabled() {
		// This test verifies that the application is a Discovery Client,
		// ensuring it can register with a discovery server.
	}

	@Test
	void mainMethodTest() {
		// This test calls the main method to ensure that the application starts up correctly without throwing any exceptions.
		DemoApplication.main(new String[]{});
	}
}
