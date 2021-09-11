package com.ironhack.homework3;

import com.ironhack.homework3.dao.main.MainMenuAutowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class Homework3ApplicationTests {

	@MockBean
	private MainMenuAutowired mainMenuAutowired;

	@Test
	void contextLoads() {
	}

}
