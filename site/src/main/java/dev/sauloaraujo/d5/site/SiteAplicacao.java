package dev.sauloaraujo.d5.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "dev.sauloaraujo.d5.site", "dev.sauloaraujo.d5.gerador.nucleo" })
public class SiteAplicacao {
	public static void main(String[] args) {
		SpringApplication.run(SiteAplicacao.class, args);
	}
}
