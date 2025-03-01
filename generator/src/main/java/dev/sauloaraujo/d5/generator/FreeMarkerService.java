package dev.sauloaraujo.d5.generator;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service
public class FreeMarkerService {
	private @Autowired Configuration configuration;

	public void process(File file, String templateName, Map<String, Object> dataModel) {
		file.getParentFile().mkdirs();
		try (var writer = new FileWriter(file, UTF_8)) {
			var template = configuration.getTemplate(templateName);
			template.process(dataModel, writer);
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}
}