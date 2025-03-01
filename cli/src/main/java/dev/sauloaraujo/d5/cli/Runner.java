package dev.sauloaraujo.d5.cli;

import java.util.Objects;

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

class A {
	int a;

	@Override
	public int hashCode() {
		return Objects.hash(a);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		A other = (A) obj;
		return a == other.a;
	}

}

class B extends A {
	Object b;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(b);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		B other = (B) obj;
		return Objects.equals(b, other.b);
	}

}