package com.ironhack.homework3;

import com.ironhack.homework3.dao.main.MainMenuAutowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Homework3Application implements CommandLineRunner {
	@Autowired
	MainMenuAutowired mainMenuAutowired;

	public static void main(String[] args) {
		SpringApplication.run(Homework3Application.class, args);
	}

	@Override
	public void run(String... args) {

//        version with the use of spring components, the better one!
		mainMenuAutowired.printMenu();
	}
}

// to run test comment above & use this one as otherwise the application runs and tests are blocked:
/*public class Homework3Application {


	public static void main(String[] args) {
		SpringApplication.run(Homework3Application.class, args);
	}

}*/
