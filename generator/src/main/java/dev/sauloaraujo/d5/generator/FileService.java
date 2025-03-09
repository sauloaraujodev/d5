package dev.sauloaraujo.d5.generator;

import static dev.sauloaraujo.d5.generator.UpperCamelToLowerHyphenMethodModel.convert;

import java.io.File;

import org.contextmapper.dsl.contextMappingDSL.BoundedContext;
import org.springframework.stereotype.Service;

@Service
class FileService {
	static final String PARENT_MODULE = "parent";
	static final String SHARED_MODULE = "domain-shared";
	static final String APPLICATION_MODULE = "application";
	static final String JPA_MODULE = "infrastructure-jpa";
	static final String VAADIN_MODULE = "presentation-vaadin";
	static final String BACKEND_MODULE = "presentation-backend";
	static final String ANGULAR_MODULE = "presentation-angular";

	static final String DOMAIN_PREFIX = "domain-";
	static final String DOMAIN_SUFFIX = "";
	static final String BDD_SUFFIX = "-bdd";

	static final String POM_FILE = "pom.xml";

	static final String SRC_DIRECTORY = "src";
	static final String MAIN_DIRECTORY = "main";
	static final String TEST_DIRECTORY = "test";
	static final String JAVA_DIRECTORY = "java";
	static final String RESOURCES_DIRECTORY = "resources";

	static final String JAVA_EXTENSION = ".java";

	File projectDirectory(String outputPath, String projectIdentifier) {
		return new File(outputPath, projectIdentifier);
	}

	File moduleDirectory(String outputPath, String projectIdentifier, String module) {
		var parent = projectDirectory(outputPath, projectIdentifier);
		return new File(parent, module);
	}

	File domainDirectory(String outputPath, String projectIdentifier, BoundedContext boundedContext) {
		return domainDirectory(outputPath, projectIdentifier, boundedContext, DOMAIN_SUFFIX);
	}

	File bddDirectory(String outputPath, String projectIdentifier, BoundedContext boundedContext) {
		return domainDirectory(outputPath, projectIdentifier, boundedContext, BDD_SUFFIX);
	}

	File domainDirectory(String outputPath, String projectIdentifier, BoundedContext boundedContext, String suffix) {
		var module = DOMAIN_PREFIX + convert(boundedContext.getName()) + suffix;
		return moduleDirectory(outputPath, projectIdentifier, module);
	}

	File projectPomFile(String outputPath, String projectIdentifier) {
		var directory = projectDirectory(outputPath, projectIdentifier);
		return directoryPomFile(directory);
	}

	File modulePomFile(String outputPath, String projectIdentifier, String module) {
		var directory = moduleDirectory(outputPath, projectIdentifier, module);
		return directoryPomFile(directory);
	}

	File domainPomFile(String outputPath, String projectIdentifier, BoundedContext boundedContext) {
		var directory = domainDirectory(outputPath, projectIdentifier, boundedContext);
		return directoryPomFile(directory);
	}

	File bddPomFile(String outputPath, String projectIdentifier, BoundedContext boundedContext) {
		var directory = bddDirectory(outputPath, projectIdentifier, boundedContext);
		return directoryPomFile(directory);
	}

	File directoryPomFile(File directory) {
		return new File(directory, POM_FILE);
	}

	File classFile(String outputPath, String projectIdentifier, String module, String mainOrTest,
			String javaOrResources, String packagePreffix, String subPackage, String className) {
		var directory = sourceDirectory(outputPath, projectIdentifier, module, mainOrTest, javaOrResources,
				packagePreffix, subPackage);
		return new File(directory, className + JAVA_EXTENSION);
	}

	File sourceDirectory(String outputPath, String projectIdentifier, String module, String mainOrTest,
			String javaOrResources, String packagePreffix, String subPackage) {
		var parent = moduleDirectory(outputPath, projectIdentifier, module);

		var child = SRC_DIRECTORY + "/" + mainOrTest + "/" + javaOrResources + "/";
		child += packagePath(packagePreffix);
		if (subPackage != null) {
			child += '.';
			child += packagePath(subPackage);
		}
		child = child.replace('.', '/');

		return new File(parent, child);
	}

	String packagePath(String p) {
		return p.replace('.', '/');
	}
}