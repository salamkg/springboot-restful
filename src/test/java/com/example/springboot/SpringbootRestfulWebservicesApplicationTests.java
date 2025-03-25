package com.example.springboot;

import com.example.springboot.controllers.TaskController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SpringbootRestfulWebservicesApplicationTests {

	@Autowired
	TaskController taskController;

	@Test
	void contextLoads() {
		assertNotNull(taskController);
	}


}
