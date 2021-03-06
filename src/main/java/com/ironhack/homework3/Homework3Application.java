package com.ironhack.homework3;

import com.ironhack.homework3.dao.main.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Homework3Application implements CommandLineRunner {
    @Autowired
	Menu menu;

    @Value("#{new Boolean('${application.test}')}")
    private Boolean applicationTest;

    public static void main(String[] args) {
        SpringApplication.run(Homework3Application.class, args);
    }

    @Override
    public void run(String... args) {
		if (!applicationTest){
            menu.mainMenu();
        }
    }
}
