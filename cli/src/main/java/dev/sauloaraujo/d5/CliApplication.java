package dev.sauloaraujo.d5;

import static java.lang.System.exit;
import static org.springframework.boot.SpringApplication.exit;
import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CliApplication {
	public static void main(String[] args) {
		exit(exit(run(CliApplication.class, args)));
	}
}