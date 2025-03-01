package dev.sauloaraujo.d5.generator;

import static dev.sauloaraujo.d5.generator.UpperCamelToLowerUnderscoreMethodModel.convert;

import java.io.File;
import java.util.HashMap;

import org.contextmapper.dsl.contextMappingDSL.Aggregate;
import org.contextmapper.dsl.contextMappingDSL.BoundedContext;
import org.contextmapper.dsl.contextMappingDSL.ContextMappingModel;
import org.contextmapper.tactic.dsl.tacticdsl.ValueObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainObjectService {
	private @Autowired FileService fileService;
	private @Autowired FreeMarkerService freeMarkerService;

	public void generate(String outputPath, String projectIdentifier, String groupId, String version,
			String packagePrefix, ContextMappingModel model) {
		for (var boundedContext : model.getBoundedContexts()) {
			for (var aggregate : boundedContext.getAggregates()) {
				for (var domainObject : aggregate.getDomainObjects()) {
					if (domainObject instanceof ValueObject) {
						var valueObject = (ValueObject) domainObject;
						generate(outputPath, projectIdentifier, packagePrefix, boundedContext, aggregate, valueObject);
					}
				}
			}
		}
	}

	public void generate(String outputPath, String projectIdentifier, String packagePrefix,
			BoundedContext boundedContext, Aggregate aggregate, ValueObject valueObject) {
		var domainDirectory = fileService.domainDirectory(outputPath, projectIdentifier, boundedContext);

		var packageName = packagePrefix + "." + convert(boundedContext.getName()) + "." + convert(aggregate.getName());
		var packagePath = packageName.replace('.', '/');

		var file = new File(domainDirectory, "src/main/java/" + packagePath + "/" + valueObject.getName() + ".java");

		var dataModel = new HashMap<String, Object>();
		dataModel.put("lowerCamelToUpperCamel", new LowerCamelToUpperCamelMethodModel());
		dataModel.put("upperCamelToLowerUnderscore", new UpperCamelToLowerUnderscoreMethodModel());
		dataModel.put("packagePrefix", packagePrefix);
		dataModel.put("boundedContext", boundedContext);
		dataModel.put("aggregate", aggregate);
		dataModel.put("valueObject", valueObject);

		freeMarkerService.process(file, "ValueObject.ftlh", dataModel);
	}
}