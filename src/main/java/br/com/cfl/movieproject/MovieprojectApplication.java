package br.com.cfl.movieproject;

import br.com.cfl.movieproject.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieprojectApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MovieprojectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();

	}
}
