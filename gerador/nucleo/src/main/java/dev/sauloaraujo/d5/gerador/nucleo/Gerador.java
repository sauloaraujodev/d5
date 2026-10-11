package dev.sauloaraujo.d5.gerador.nucleo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@Component
public class Gerador {
	private final Configuration freemarker;

	public Gerador() {
		freemarker = new Configuration(Configuration.VERSION_2_3_34);
		freemarker.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
		freemarker.setDefaultEncoding(StandardCharsets.UTF_8.name());
		freemarker.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}

	public void gerar(File diretorio, String groupId, String nome, Set<String> subdominios, boolean framework) {
		Objects.requireNonNull(diretorio);
		Objects.requireNonNull(groupId);
		Objects.requireNonNull(nome);
		Objects.requireNonNull(subdominios);
		if (subdominios.isEmpty()) {
			throw new IllegalArgumentException("É necessário informar ao menos um subdomínio");
		}

		var raiz = new File(diretorio, nome);
		var subdominiosOrdenados = new TreeSet<>(subdominios);
		var artifactIdCompartilhado = nome + "-dominio-compartilhado";

		gerarPomRaiz(raiz, groupId, nome, subdominiosOrdenados, framework);

		gerarDominioCompartilhado(raiz, groupId, nome, artifactIdCompartilhado, framework);
		for (var subdominio : subdominiosOrdenados) {
			gerarDominio(raiz, groupId, nome, subdominio, artifactIdCompartilhado, framework);
		}

		gerarAplicacao(raiz, groupId, nome, subdominiosOrdenados, framework);
		gerarInfraestrutura(raiz, groupId, nome, framework);
		gerarUiBackend(raiz, groupId, nome, framework);
	}

	private void gerarPomRaiz(File raiz, String groupId, String nome, Set<String> subdominios, boolean framework) {
		processar("pom-raiz.ftl",
				Map.of("groupId", groupId, "nome", nome, "subdominios", subdominios, "framework", framework),
				new File(raiz, "pom.xml"));
	}

	private void gerarDominioCompartilhado(File raiz, String groupId, String nome, String artifactId,
			boolean framework) {
		var modulo = new File(raiz, "dominio-compartilhado");

		gerarPomDominio(modulo, groupId, nome, artifactId, null, framework);

		var pacote = groupId + "." + nome + ".dominio";
		var mainJava = new File(modulo, "src/main/java/" + pacote.replace('.', '/'));
		processar("package-info.java.ftl", Map.of("pacote", pacote), new File(mainJava, "package-info.java"));
		processar("Autorizacao.java.ftl", Map.of("pacote", pacote), new File(mainJava, "Autorizacao.java"));

		criarDiretorioVazio(new File(modulo, "src/main/resources"));
		criarDiretorioVazio(new File(modulo, "src/test/java"));
		criarDiretorioVazio(new File(modulo, "src/test/resources"));
	}

	private void gerarDominio(File raiz, String groupId, String nome, String subdominio, String artifactIdCompartilhado,
			boolean framework) {
		var modulo = new File(raiz, "dominio-" + subdominio);

		gerarPomDominio(modulo, groupId, nome, nome + "-dominio-" + subdominio, artifactIdCompartilhado, framework);

		criarDiretorioVazio(new File(modulo, "src/main/java"));
		criarDiretorioVazio(new File(modulo, "src/main/resources"));
		criarDiretorioVazio(new File(modulo, "src/test/resources"));

		var pacote = groupId + "." + nome + ".dominio." + subdominio;
		var testeJava = new File(modulo, "src/test/java/" + pacote.replace('.', '/'));
		processar("CucumberTests.java.ftl", Map.of("pacote", pacote), new File(testeJava, "CucumberTests.java"));
	}

	private void gerarPomDominio(File modulo, String groupId, String nome, String artifactId,
			@Nullable String dominioCompartilhado, boolean framework) {
		var modelo = new HashMap<String, Object>();
		modelo.put("groupId", groupId);
		modelo.put("nome", nome);
		modelo.put("artifactId", artifactId);
		modelo.put("dominioCompartilhado", dominioCompartilhado);
		modelo.put("framework", framework);
		processar("pom-dominio.ftl", modelo, new File(modulo, "pom.xml"));
	}

	private void gerarAplicacao(File raiz, String groupId, String nome, Set<String> subdominios, boolean framework) {
		processar("pom-aplicacao.ftl",
				Map.of("groupId", groupId, "nome", nome, "subdominios", subdominios, "framework", framework),
				new File(raiz, "aplicacao/pom.xml"));
	}

	private void gerarInfraestrutura(File raiz, String groupId, String nome, boolean framework) {
		var modulo = new File(raiz, "infraestrutura");

		processar("pom-infraestrutura.ftl", Map.of("groupId", groupId, "nome", nome, "framework", framework),
				new File(modulo, "pom.xml"));

		var pacote = groupId + "." + nome + ".infraestrutura";
		var testeJava = new File(modulo, "src/test/java/" + pacote.replace('.', '/'));
		processar("InfraestruturaTesteAplicacao.java.ftl", Map.of("pacote", pacote),
				new File(testeJava, "InfraestruturaTesteAplicacao.java"));
	}

	private void gerarUiBackend(File raiz, String groupId, String nome, boolean framework) {
		var modulo = new File(raiz, "ui-backend");

		processar("pom-ui-backend.ftl", Map.of("groupId", groupId, "nome", nome, "framework", framework),
				new File(modulo, "pom.xml"));

		var pacote = groupId + "." + nome;
		var raizJava = new File(modulo, "src/main/java/" + pacote.replace('.', '/'));

		processar("package-info.java.ftl", Map.of("pacote", pacote), new File(raizJava, "package-info.java"));
		processar("Aplicacao.java.ftl", Map.of("pacote", pacote), new File(raizJava, "BackendAplicacao.java"));

		if (framework) {
			var pacoteSeguranca = pacote + ".ui.backend.seguranca";
			var seguranca = new File(raizJava, "ui/backend/seguranca");

			processar("package-info.java.ftl", Map.of("pacote", pacoteSeguranca),
					new File(seguranca, "package-info.java"));
			processar("SegurancaConfiguracao.java.ftl", Map.of("pacote", pacote),
					new File(seguranca, "SegurancaConfiguracao.java"));
			processar("AutenticacaoControlador.java.ftl", Map.of("pacote", pacote),
					new File(seguranca, "AutenticacaoControlador.java"));
		}
	}

	private void processar(String template, Map<String, Object> modelo, File destino) {
		try {
			Files.createDirectories(destino.getParentFile().toPath());

			var modeloTemplate = freemarker.getTemplate(template);
			try (var escritor = new OutputStreamWriter(new FileOutputStream(destino), StandardCharsets.UTF_8)) {
				modeloTemplate.process(modelo, escritor);
			}
		} catch (IOException | TemplateException e) {
			throw new IllegalStateException("Falha ao gerar " + destino, e);
		}
	}

	private void criarDiretorioVazio(File diretorio) {
		try {
			Files.createDirectories(diretorio.toPath());
			Files.createFile(new File(diretorio, ".gitkeep").toPath());
		} catch (IOException e) {
			throw new IllegalStateException("Falha ao gerar " + diretorio, e);
		}
	}
}
