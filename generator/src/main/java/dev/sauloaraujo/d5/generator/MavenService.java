package dev.sauloaraujo.d5.generator;

import static dev.sauloaraujo.d5.generator.FileService.ANGULAR_MODULE;
import static dev.sauloaraujo.d5.generator.FileService.APPLICATION_MODULE;
import static dev.sauloaraujo.d5.generator.FileService.BACKEND_MODULE;
import static dev.sauloaraujo.d5.generator.FileService.JAVA_DIRECTORY;
import static dev.sauloaraujo.d5.generator.FileService.JPA_MODULE;
import static dev.sauloaraujo.d5.generator.FileService.MAIN_DIRECTORY;
import static dev.sauloaraujo.d5.generator.FileService.PARENT_MODULE;
import static dev.sauloaraujo.d5.generator.FileService.SHARED_MODULE;
import static dev.sauloaraujo.d5.generator.FileService.VAADIN_MODULE;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.contextmapper.dsl.contextMappingDSL.BoundedContext;
import org.contextmapper.dsl.contextMappingDSL.ContextMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MavenService {
	private @Autowired FileService fileService;
	private @Autowired FreeMarkerService freeMarkerService;

	public void generate(String outputPath, String projectIdentifier, String groupId, String version,
			String packagePrefix, ContextMappingModel model) {
		var dataModel = new HashMap<String, Object>();
		dataModel.put("projectIdentifier", projectIdentifier);
		dataModel.put("groupId", groupId);
		dataModel.put("version", version);
		dataModel.put("packagePrefix", packagePrefix);
		dataModel.put("model", model);

		generate(fileService.projectPomFile(outputPath, projectIdentifier), "modules.ftlh", dataModel, false);
		generate(fileService.modulePomFile(outputPath, projectIdentifier, PARENT_MODULE), "parent.ftlh", dataModel);
		generate(fileService.modulePomFile(outputPath, projectIdentifier, SHARED_MODULE), "shared.ftlh", dataModel);

		for (var boudedContext : model.getBoundedContexts()) {
			generate(outputPath, projectIdentifier, boudedContext, dataModel);
		}

		generate(fileService.modulePomFile(outputPath, projectIdentifier, APPLICATION_MODULE), "application.ftlh",
				dataModel);
		generate(fileService.modulePomFile(outputPath, projectIdentifier, JPA_MODULE), "jpa.ftlh", dataModel);

		generate(fileService.modulePomFile(outputPath, projectIdentifier, VAADIN_MODULE), "vaadin.ftlh", dataModel);
		freeMarkerService
				.process(
						fileService.classFile(outputPath, projectIdentifier, "presentation-vaadin", MAIN_DIRECTORY,
								JAVA_DIRECTORY, packagePrefix, null, "VaadinApplication"),
						"VaadinApplication.ftlh", dataModel);

		generate(fileService.modulePomFile(outputPath, projectIdentifier, BACKEND_MODULE), "backend.ftlh", dataModel);
		generate(fileService.modulePomFile(outputPath, projectIdentifier, ANGULAR_MODULE), "angular.ftlh", dataModel);
	}

	private void generate(File file, String templateName, Map<String, Object> model) {
		generate(file, templateName, model, true);
	}

	private void generate(File file, String templateName, Map<String, Object> dataModel, boolean subdirectories) {
		if (subdirectories) {
			var parent = file.getParentFile();

			new File(parent, "src/main/java").mkdirs();
			new File(parent, "src/main/resources").mkdirs();

			new File(parent, "src/test/java").mkdirs();
			new File(parent, "src/test/resources").mkdirs();
		}

		freeMarkerService.process(file, templateName, dataModel);
	}

	private void generate(String outputPath, String projectIdentifier, BoundedContext boundedContext,
			Map<String, Object> dataModel) {
		dataModel.put("boundedContext", boundedContext);
		generate(fileService.domainPomFile(outputPath, projectIdentifier, boundedContext), "domain.ftlh", dataModel);
	}
}