package br.com.cfl.movieproject;

import br.com.cfl.movieproject.principal.Principal;
import br.com.cfl.movieproject.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieprojectApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repositorio;

	public static void main(String[] args) {
		SpringApplication.run(MovieprojectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();

	}
}
