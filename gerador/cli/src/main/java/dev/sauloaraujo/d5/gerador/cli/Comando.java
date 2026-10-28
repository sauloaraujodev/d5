package dev.sauloaraujo.d5.gerador.cli;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import dev.sauloaraujo.d5.gerador.nucleo.Frontend;
import dev.sauloaraujo.d5.gerador.nucleo.Gerador;
import dev.sauloaraujo.d5.gerador.nucleo.Subdominio;
import dev.sauloaraujo.d5.gerador.nucleo.TipoSubdominio;
import picocli.CommandLine.ArgGroup;
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

	@Option(names = { "-p",
			"--descricao-projeto" }, required = true, description = "A descrição do projeto, incluída em AGENTS.md e docs/projeto.cml.")
	private @Nullable String descricao;

	@Option(names = { "-v",
			"--versao" }, defaultValue = "1.0.0-SNAPSHOT", description = "A versão do projeto, no formato do versionamento semântico.")
	private @Nullable String versao;

	// Cada subdomínio é um grupo de opções: -s nome -y tipo -e descrição. O grupo se repete para
	// cada subdomínio.
	@ArgGroup(exclusive = false, multiplicity = "1..*")
	private @Nullable List<SubdominioOpcoes> subdominios;

	@Option(names = { "-f",
			"--framework" }, arity = "1", defaultValue = "true", description = "Se o projeto gerado deve depender do framework D5.")
	private boolean framework;

	@Option(names = { "-a",
			"--agentes" }, arity = "1", defaultValue = "true", description = "Se o projeto gerado deve incluir o AGENTS.md e os skills.")
	private boolean agentes;

	@Option(names = { "-t",
			"--frontend" }, defaultValue = "angular", description = "A tecnologia do frontend: angular ou react.")
	private @Nullable String frontend;

	static class SubdominioOpcoes {
		@Option(names = { "-s", "--subdominio" }, required = true, description = "O nome de um subdomínio do domínio do projeto.")
		private @Nullable String nome;

		@Option(names = { "-y",
				"--tipo" }, required = true, description = "O tipo do subdomínio: ${COMPLETION-CANDIDATES}.")
		private @Nullable TipoSubdominio tipo;

		@Option(names = { "-e",
				"--descricao" }, required = true, description = "A descrição do subdomínio, incluída em docs/projeto.cml.")
		private @Nullable String descricao;

		Subdominio paraSubdominio() {
			return new Subdominio(Objects.requireNonNull(nome), Objects.requireNonNull(tipo),
					Objects.requireNonNull(descricao));
		}
	}

	@Override
	public Integer call() throws Exception {
		var subdominiosInformados = Objects.requireNonNull(subdominios).stream().map(SubdominioOpcoes::paraSubdominio)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		gerador.gerar(Objects.requireNonNull(diretorio), Objects.requireNonNull(groupId), Objects.requireNonNull(nome),
				Objects.requireNonNull(descricao), Objects.requireNonNull(versao), subdominiosInformados, framework, agentes,
				Frontend.de(Objects.requireNonNull(frontend)));
		return 0;
	}
}