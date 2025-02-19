package dev.sauloaraujo.d5.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@Component
public class Runner implements CommandLineRunner, ExitCodeGenerator {
	private int exitCode;

	private @Autowired GenerateCommand command;
	private @Autowired IFactory factory;

	@Override
	public void run(String... args) throws Exception {
		exitCode = new CommandLine(command, factory).execute(args);
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}
}