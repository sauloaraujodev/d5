package dev.sauloaraujo.d5.gerador.cli;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import dev.sauloaraujo.d5.gerador.nucleo.Gerador;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "D5", mixinStandardHelpOptions = true, version = "1.0.0-SNAPSHOT", description = "DDD-Driven Development")
public class Comando implements Callable<Integer> {
	private final Gerador gerador;

	public Comando(Gerador gerador) {
		this.gerador = Objects.requireNonNull(gerador);
	}

	@Option(names = { "-d", "--diretorio" }, required = true, description = "O diretório onde o projeto será gerado.")
	private @Nullable File diretorio;

	@Option(names = { "-g", "--groupId" }, required = true, description = "O groupId do projeto.")
	private @Nullable String groupId;

	@Option(names = { "-n", "--nome" }, required = true, description = "O nome do projeto.")
	private @Nullable String nome;

	@Option(names = { "-s", "--subdominio" }, required = true, description = "Os subdomínios do domínio do projeto.")
	private @Nullable Set<String> subdominios;

	@Option(names = { "-f",
			"--framework" }, arity = "1", defaultValue = "true", description = "Se o projeto gerado deve depender do framework D5.")
	private boolean framework;

	@Override
	public Integer call() throws Exception {
		gerador.gerar(Objects.requireNonNull(diretorio), Objects.requireNonNull(groupId), Objects.requireNonNull(nome),
				Objects.requireNonNull(subdominios), framework);
		return 0;
	}
}