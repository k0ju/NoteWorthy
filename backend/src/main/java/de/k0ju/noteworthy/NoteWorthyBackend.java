package de.k0ju.noteworthy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class NoteWorthyBackend {

	public static void main(String[] args) {
		SpringApplication.run(NoteWorthyBackend.class, args);
	}

}