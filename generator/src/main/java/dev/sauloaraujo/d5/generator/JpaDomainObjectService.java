package dev.sauloaraujo.d5.generator;

import static dev.sauloaraujo.d5.generator.FileService.INFRASTRUCTURE_MODULE;
import static dev.sauloaraujo.d5.generator.FileService.JAVA_DIRECTORY;
import static dev.sauloaraujo.d5.generator.FileService.MAIN_DIRECTORY;
import static dev.sauloaraujo.d5.generator.UpperCamelToLowerUnderscoreMethodModel.convert;

import java.util.HashMap;
import java.util.Set;

import org.contextmapper.dsl.contextMappingDSL.Aggregate;
import org.contextmapper.dsl.contextMappingDSL.BoundedContext;
import org.contextmapper.dsl.contextMappingDSL.ContextMappingModel;
import org.contextmapper.tactic.dsl.tacticdsl.Entity;
import org.contextmapper.tactic.dsl.tacticdsl.ValueObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaDomainObjectService {
	private @Autowired FileService fileService;
	private @Autowired FreeMarkerService freeMarkerService;
	private @Autowired SharedKernelService sharedKernelService;

	public void generate(String outputPath, String projectIdentifier, String groupId, String version,
			String packagePrefix, ContextMappingModel model) {
		var sharedValueObjects = sharedKernelService.sharedValueObjects(model);

		for (var boundedContext : model.getBoundedContexts()) {
			for (var aggregate : boundedContext.getAggregates()) {
				for (var domainObject : aggregate.getDomainObjects()) {
					if (domainObject instanceof ValueObject) {
						var valueObject = (ValueObject) domainObject;
						generate(outputPath, projectIdentifier, packagePrefix, sharedValueObjects, boundedContext,
								aggregate, valueObject);
					} else if (domainObject instanceof Entity) {
						var entity = (Entity) domainObject;
						generate(outputPath, projectIdentifier, packagePrefix, boundedContext, aggregate, entity);
					}
				}
			}
		}
	}

	public void generate(String outputPath, String projectIdentifier, String packagePrefix,
			Set<ValueObject> sharedValueObjects, BoundedContext boundedContext, Aggregate aggregate,
			ValueObject valueObject) {
		var subPackage = "infrastructure.persistence." + convert(boundedContext.getName()) + "."
				+ convert(aggregate.getName());

		var file = fileService.classFile(outputPath, projectIdentifier, INFRASTRUCTURE_MODULE, MAIN_DIRECTORY,
				JAVA_DIRECTORY, packagePrefix, subPackage, valueObject.getName());

		var dataModel = new HashMap<String, Object>();
		dataModel.put("attributesOrReferences", new AttributesOrReferencesMethodModel(packagePrefix));
		dataModel.put("packagePrefix", packagePrefix);
		dataModel.put("boundedContext", boundedContext);
		dataModel.put("aggregate", aggregate);
		dataModel.put("valueObject", valueObject);

		freeMarkerService.process(file, "JpaValueObject.ftlh", dataModel);
	}

	public void generate(String outputPath, String projectIdentifier, String packagePrefix,
			BoundedContext boundedContext, Aggregate aggregate, Entity entity) {
		var module = fileService.domainModule(boundedContext);

		var subPackage = "infrastructure.persistence." + convert(boundedContext.getName()) + "."
				+ convert(aggregate.getName());

		var file = fileService.classFile(outputPath, projectIdentifier, module, MAIN_DIRECTORY, JAVA_DIRECTORY,
				packagePrefix, subPackage, entity.getName());

		var dataModel = new HashMap<String, Object>();
		dataModel.put("attributesOrReferences", new AttributesOrReferencesMethodModel(packagePrefix));
		dataModel.put("entityId", new EntityIdMethodModel(packagePrefix));
		dataModel.put("packagePrefix", packagePrefix);
		dataModel.put("boundedContext", boundedContext);
		dataModel.put("aggregate", aggregate);
		dataModel.put("entity", entity);

		freeMarkerService.process(file, "JpaEntity.ftlh", dataModel);

//		if (entity.isAggregateRoot()) {
//			file = fileService.classFile(outputPath, projectIdentifier, module, MAIN_DIRECTORY, JAVA_DIRECTORY,
//					packagePrefix, subPackage, entity.getName() + "Repository");
//			freeMarkerService.process(file, "Repository.ftlh", dataModel);
//
//			file = fileService.classFile(outputPath, projectIdentifier, module, MAIN_DIRECTORY, JAVA_DIRECTORY,
//					packagePrefix, subPackage, entity.getName() + "DomainService");
//			freeMarkerService.process(file, "DomainService.ftlh", dataModel);
//		}
	}
}