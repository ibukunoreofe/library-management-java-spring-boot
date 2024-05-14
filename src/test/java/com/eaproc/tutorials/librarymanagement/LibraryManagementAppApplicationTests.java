package com.eaproc.tutorials.librarymanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LibraryManagementAppApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		// Verify that the application context loads successfully
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void testBeansPresence() {
		// Example of checking if certain beans are present in the context
		assertThat(applicationContext.containsBean("roleRepository")).isTrue();
		assertThat(applicationContext.containsBean("userRepository")).isTrue();
		assertThat(applicationContext.containsBean("bookRepository")).isTrue();
		assertThat(applicationContext.containsBean("checkoutRepository")).isTrue();
	}
}
