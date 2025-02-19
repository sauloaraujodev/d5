package dev.sauloaraujo.d5.web;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.FileUtils.write;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.ResponseEntity.ok;
import static org.zeroturnaround.zip.ZipUtil.pack;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.sauloaraujo.d5.generator.GeneratorService;

@Controller
public class GeneratorController {
	private @Autowired GeneratorService service;

	@PostMapping(value = "generate")
	public ResponseEntity<FileSystemResource> generate(@RequestParam("projectIdentifier") String projectIdentifier,
			@RequestParam("groupId") String groupId, @RequestParam("version") String version,
			@RequestParam("packagePrefix") String packagePrefix, @RequestParam("cmlModel") String cmlModel)
			throws IOException {
		var inputFile = createTempFile("D5", ".cml").toFile();
		inputFile.deleteOnExit();
		write(inputFile, cmlModel, UTF_8);

		var outputDirectory = createTempDirectory("D5").toFile();
		outputDirectory.deleteOnExit();
		var outputPath = outputDirectory.getPath();

		service.generate(outputPath, projectIdentifier, groupId, version, packagePrefix, inputFile);

		var zipOutputFile = createTempFile("D5", ".zip").toFile();
		zipOutputFile.deleteOnExit();
		pack(outputDirectory, zipOutputFile);

		var type = new MediaType("application", "zip");
		var disposition = "attachment; filename=" + projectIdentifier + ".zip";
		return ok().contentType(type).header(CONTENT_DISPOSITION, disposition)
				.body(new FileSystemResource(zipOutputFile));
	}
}