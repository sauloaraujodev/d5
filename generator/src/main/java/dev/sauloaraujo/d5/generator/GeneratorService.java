package dev.sauloaraujo.d5.generator;

import static dev.sauloaraujo.d5.generator.UpperCamelToLowerHyphenMethodModel.convert;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.contextmapper.dsl.standalone.ContextMapperStandaloneSetup.getStandaloneAPI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.contextmapper.dsl.contextMappingDSL.BoundedContext;
import org.contextmapper.dsl.contextMappingDSL.ContextMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service
public class GeneratorService {
	private @Autowired Configuration configuration;

	public void generate(String outputPath, String projectIdentifier, String groupId, String version,
			String packagePrefix, File cmlFile) {
		var outputDirectory = new File(outputPath, projectIdentifier);
		var api = getStandaloneAPI();
		var resource = api.loadCML(cmlFile);
		var model = resource.getContextMappingModel();
		generate(outputDirectory, projectIdentifier, groupId, version, packagePrefix, model);
	}

	private void generate(File outputDirectory, String projectIdentifier, String groupId, String version,
			String packagePrefix, ContextMappingModel model) {
		var dataModel = new HashMap<String, Object>();
		dataModel.put("upperCamelToLowerHyphen", new UpperCamelToLowerHyphenMethodModel());
		dataModel.put("projectIdentifier", projectIdentifier);
		dataModel.put("groupId", groupId);
		dataModel.put("version", version);
		dataModel.put("packagePrefix", packagePrefix);
		dataModel.put("model", model);

		generate(new File(outputDirectory, "pom.xml"), "modules.ftlh", dataModel, false);
		generate(new File(outputDirectory, "parent/pom.xml"), "parent.ftlh", dataModel);
		generate(new File(outputDirectory, "domain-common/pom.xml"), "common.ftlh", dataModel);

		for (var context : model.getBoundedContexts()) {
			generate(outputDirectory, dataModel, context);
		}

		generate(new File(outputDirectory, "application/pom.xml"), "application.ftlh", dataModel);
		generate(new File(outputDirectory, "infrastructure-jpa/pom.xml"), "jpa.ftlh", dataModel);

		generate(new File(outputDirectory, "presentation-vaadin/pom.xml"), "vaadin.ftlh", dataModel);
		generate(new File(outputDirectory,
				"presentation-vaadin/src/main/java/" + packagePrefix.replace('.', '/') + "/VaadinApplication.java"),
				"VaadinApplication.ftlh", dataModel, false);

		generate(new File(outputDirectory, "presentation-backend/pom.xml"), "backend.ftlh", dataModel);
		generate(new File(outputDirectory, "presentation-angular/pom.xml"), "angular.ftlh", dataModel);
	}

	private void generate(File outputFile, String templateName, Map<String, Object> model) {
		generate(outputFile, templateName, model, true);
	}

	private void generate(File outputFile, String templateName, Map<String, Object> model, boolean subdirectories) {
		var parentFile = outputFile.getParentFile();
		parentFile.mkdirs();

		if (subdirectories) {
			new File(parentFile, "src/main/java").mkdirs();
			new File(parentFile, "src/main/resources").mkdirs();

			new File(parentFile, "src/test/java").mkdirs();
			new File(parentFile, "src/test/resources").mkdirs();
		}

		try (var writer = new FileWriter(outputFile, UTF_8)) {
			var template = configuration.getTemplate(templateName);
			template.process(model, writer);
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	private void generate(File outputDirectory, Map<String, Object> dataModel, BoundedContext context) {
		var name = context.getName();
		name = convert(name);
		dataModel.put("contextName", name);
		generate(new File(outputDirectory, "domain-" + name + "/pom.xml"), "context.ftlh", dataModel);
		generate(new File(outputDirectory, "domain-" + name + "-bdd/pom.xml"), "context-bdd.ftlh", dataModel);
	}
}