package dev.sauloaraujo.d5.generator;

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
		dataModel.put("upperCamelToLowerHyphen", new UpperCamelToLowerHyphenMethodModel());
		dataModel.put("projectIdentifier", projectIdentifier);
		dataModel.put("groupId", groupId);
		dataModel.put("version", version);
		dataModel.put("packagePrefix", packagePrefix);
		dataModel.put("model", model);

		generate(fileService.projectPomFile(outputPath, projectIdentifier), "modules.ftlh", dataModel, false);
		generate(fileService.modulePomFile(outputPath, projectIdentifier, "parent"), "parent.ftlh", dataModel);
		generate(fileService.modulePomFile(outputPath, projectIdentifier, "domain-common"), "common.ftlh", dataModel);

		for (var boudedContext : model.getBoundedContexts()) {
			generate(outputPath, projectIdentifier, boudedContext, dataModel);
		}

		generate(fileService.modulePomFile(outputPath, projectIdentifier, "application"), "application.ftlh",
				dataModel);
		generate(fileService.modulePomFile(outputPath, projectIdentifier, "infrastructure-jpa"), "jpa.ftlh", dataModel);

		generate(fileService.modulePomFile(outputPath, projectIdentifier, "presentation-vaadin"), "vaadin.ftlh",
				dataModel);
		freeMarkerService
				.process(
						fileService.moduleMainJavaSourceFile(outputPath, projectIdentifier, "presentation-vaadin",
								packagePrefix, null, "VaadinApplication"),
						"VaadinApplication.ftlh", dataModel);

		generate(fileService.modulePomFile(outputPath, projectIdentifier, "presentation-backend"), "backend.ftlh",
				dataModel);
		generate(fileService.modulePomFile(outputPath, projectIdentifier, "presentation-angular"), "angular.ftlh",
				dataModel);
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
		generate(fileService.bddPomFile(outputPath, projectIdentifier, boundedContext), "domain-bdd.ftlh", dataModel);
	}
}