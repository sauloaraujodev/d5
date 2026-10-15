package dev.sauloaraujo.d5.site;

import static org.springframework.http.ResponseEntity.ok;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sauloaraujo.d5.gerador.nucleo.Gerador;

@RestController
@RequestMapping("api/gerador")
public class Controlador {
	private final Gerador gerador;

	public Controlador(Gerador gerador) {
		this.gerador = Objects.requireNonNull(gerador);
	}

	@PostMapping
	public ResponseEntity<Resource> generate(Formulario formulario) throws IOException {
		var nome = formulario.getNome();

		// Gera diretamente dentro do zip, evitando a escrita de centenas de arquivos
		// temporários
		var arquivo = Files.createTempFile("d5", ".zip");
		try {
			Files.delete(arquivo);
			try (var zip = FileSystems.newFileSystem(URI.create("jar:" + arquivo.toUri()), Map.of("create", "true"))) {
				gerador.gerar(zip.getPath("/"), formulario.getGroupId(), nome, formulario.getSubdominios(),
						formulario.isFramework(), formulario.isAgentes());
			}
			var corpo = new ByteArrayResource(Files.readAllBytes(arquivo));
			var disposicao = "attachment; filename=" + nome + ".zip";
			return ok().header(HttpHeaders.CONTENT_DISPOSITION, disposicao)
					.contentType(new MediaType("application", "zip")).body(corpo);
		} finally {
			Files.deleteIfExists(arquivo);
		}
	}
}
