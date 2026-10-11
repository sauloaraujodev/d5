package dev.sauloaraujo.d5.gerador.web;

import static org.springframework.http.ResponseEntity.ok;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sauloaraujo.d5.gerador.nucleo.Gerador;
import net.lingala.zip4j.ZipFile;

@RestController
@RequestMapping("gerador")
public class Controlador {
	private final Gerador gerador;

	public Controlador(Gerador gerador) {
		this.gerador = Objects.requireNonNull(gerador);
	}

	@PostMapping
	public ResponseEntity<Resource> generate(Formulario formulario) throws IOException {
		var diretorio = Files.createTempDirectory("d5").toFile();
		var groupId = formulario.getGroupId();
		var nome = formulario.getNome();
		var subdominios = formulario.getSubdominios();
		var framework = formulario.isFramework();

		gerador.gerar(diretorio, groupId, nome, subdominios, framework);

		var disposicao = "attachment; filename=" + nome + ".zip";
		var tipo = new MediaType("application", "zip");

		var arquivo = Files.createTempFile("d5", ".zip").toFile();
		try (var zip = new ZipFile(arquivo)) {
			diretorio = new File(diretorio, nome);
			zip.addFolder(diretorio);
		}
		var corpo = new FileSystemResource(arquivo);

		return ok().header(HttpHeaders.CONTENT_DISPOSITION, disposicao).contentType(tipo).body(corpo);
	}
}