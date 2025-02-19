package dev.sauloaraujo.d5.cli;

import java.io.File;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.sauloaraujo.d5.generator.GeneratorService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true, version = "0.0.1-SNAPSHOT")
@Component
public class GenerateCommand implements Callable<Void> {
	@Option(names = "-o", description = "output directory path", required = true)
	private String outputPath;

	@Option(names = "-i", description = "project identifier", required = true)
	private String projectIdentifier;

	@Option(names = "-g", description = "groupId", required = true)
	private String groupId;

	@Option(names = "-v", description = "version", defaultValue = "0.0.1-SNAPSHOT")
	private String version;

	@Option(names = "-p", description = "package prefix", required = true)
	private String packagePrefix;

	@Option(names = "-f", description = "input file path", required = true)
	private String inputPath;

	private @Autowired GeneratorService service;

	@Override
	public Void call() throws Exception {
		var inputFile = new File(inputPath);
		service.generate(outputPath, projectIdentifier, groupId, version, packagePrefix, inputFile);
		return null;
	}
}