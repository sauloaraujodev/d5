package dev.sauloaraujo.d5.generator;

import static dev.sauloaraujo.d5.generator.UpperCamelToLowerHyphenMethodModel.convert;

import java.io.File;

import org.contextmapper.dsl.contextMappingDSL.BoundedContext;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	public File projectPomFile(String outputPath, String projectIdentifier) {
		var directory = projectDirectory(outputPath, projectIdentifier);
		return directoryPomFile(directory);
	}

	private File directoryPomFile(File directory) {
		return new File(directory, "pom.xml");
	}

	public File modulePomFile(String outputPath, String projectIdentifier, String module) {
		var directory = moduleDirectory(outputPath, projectIdentifier, module);
		return directoryPomFile(directory);
	}

	public File domainPomFile(String outputPath, String projectIdentifier, BoundedContext boundedContext) {
		var directory = domainDirectory(outputPath, projectIdentifier, boundedContext);
		return directoryPomFile(directory);
	}

	public File bddPomFile(String outputPath, String projectIdentifier, BoundedContext boundedContext) {
		var directory = bddDirectory(outputPath, projectIdentifier, boundedContext);
		return directoryPomFile(directory);
	}

	public File projectDirectory(String outputPath, String projectIdentifier) {
		return new File(outputPath, projectIdentifier);
	}

	public File moduleDirectory(String outputPath, String projectIdentifier, String module) {
		var parent = projectDirectory(outputPath, projectIdentifier);
		return new File(parent, module);
	}

	public File domainDirectory(String outputPath, String projectIdentifier, BoundedContext boundedContext) {
		return domainDirectory(outputPath, projectIdentifier, boundedContext, "");
	}

	public File bddDirectory(String outputPath, String projectIdentifier, BoundedContext boundedContext) {
		return domainDirectory(outputPath, projectIdentifier, boundedContext, "-bdd");
	}

	private File domainDirectory(String outputPath, String projectIdentifier, BoundedContext boundedContext,
			String suffix) {
		var module = "domain-" + convert(boundedContext.getName()) + suffix;
		return moduleDirectory(outputPath, projectIdentifier, module);
	}

	public File moduleMainJavaSourceDirectory(String outputPath, String projectIdentifier, String module,
			String packagePreffix, String subPackage) {
		var parent = moduleDirectory(outputPath, projectIdentifier, module);

		var child = "src/main/java/";
		child += packagePath(packagePreffix);
		if (subPackage != null) {
			child += '.';
			child += packagePath(subPackage);
		}
		child = child.replace('.', '/');

		return new File(parent, child);
	}

	private String packagePath(String p) {
		return p.replace('.', '/');
	}

	public File moduleMainJavaSourceFile(String outputPath, String projectIdentifier, String module,
			String packagePreffix, String subPackage, String className) {
		var directory = moduleMainJavaSourceDirectory(outputPath, projectIdentifier, module, packagePreffix,
				subPackage);
		return new File(directory, className + ".java");
	}

	public File domainSharedDirectory(String outputPath, String projectIdentifier) {
		return moduleDirectory(outputPath, projectIdentifier, "domain-common");
	}
}