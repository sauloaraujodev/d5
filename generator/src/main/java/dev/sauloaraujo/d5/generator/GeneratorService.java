package dev.sauloaraujo.d5.generator;

import static org.contextmapper.dsl.standalone.ContextMapperStandaloneSetup.getStandaloneAPI;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {
	private @Autowired MavenService mavenService;
	private @Autowired DomainObjectService domainObjectService;

	public void generate(String outputPath, String projectIdentifier, String groupId, String version,
			String packagePrefix, File cmlFile) {
		var api = getStandaloneAPI();
		var resource = api.loadCML(cmlFile);
		var model = resource.getContextMappingModel();
		mavenService.generate(outputPath, projectIdentifier, groupId, version, packagePrefix, model);
		domainObjectService.generate(outputPath, projectIdentifier, groupId, version, packagePrefix, model);
	}
}