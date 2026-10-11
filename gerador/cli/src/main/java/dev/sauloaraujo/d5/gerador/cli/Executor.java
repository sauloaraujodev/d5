package dev.sauloaraujo.d5.gerador.cli;

import java.util.Objects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@Component
public class Executor implements CommandLineRunner, ExitCodeGenerator {
	private final Comando comando;
	private final IFactory fabrica;
	private int codigo;

	public Executor(Comando comando, IFactory fabrica) {
		this.comando = Objects.requireNonNull(comando);
		this.fabrica = Objects.requireNonNull(fabrica);
	}

	@Override
	public void run(String... args) throws Exception {
		var linha = new CommandLine(comando, fabrica);
		codigo = linha.execute(args);
	}

	@Override
	public int getExitCode() {
		return codigo;
	}
}