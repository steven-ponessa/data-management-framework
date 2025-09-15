package com.ibm.wfm.ms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"STUB_DIR = /test/stub"})
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
