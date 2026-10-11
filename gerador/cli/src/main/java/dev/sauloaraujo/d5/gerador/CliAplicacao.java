package dev.sauloaraujo.d5.gerador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CliAplicacao {
	public static void main(String[] args) {
		var contexto = SpringApplication.run(CliAplicacao.class, args);
		var codigo = SpringApplication.exit(contexto);
		System.exit(codigo);
	}
}