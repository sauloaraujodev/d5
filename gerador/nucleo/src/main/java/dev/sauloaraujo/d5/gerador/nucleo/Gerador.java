package dev.sauloaraujo.d5.gerador.nucleo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.jspecify.annotations.Nullable;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@Component
public class Gerador {
	private static final List<String> DIRETORIOS_PADRAO = List.of("src/main/java", "src/main/resources",
			"src/test/java", "src/test/resources");

	private static final String DIRETORIO_SKILLS = "agents/skills/";

	private static final List<String> SKILLS = List.of("especificar-funcionalidade/SKILL.md",
			"especificar-funcionalidade/references/REFERENCE.md", "especificar-testes/SKILL.md",
			"projetar-software/SKILL.md", "projetar-software/references/REFERENCE.md",
			"projetar-contextos-delimitados/SKILL.md", "projetar-objetos-valor/SKILL.md", "projetar-entidades/SKILL.md",
			"projetar-agregados/SKILL.md", "projetar-associacoes/SKILL.md", "projetar-servicos/SKILL.md",
			"projetar-repositorios/SKILL.md", "validar-modelo/SKILL.md", "validar-modelo/scripts/pom.xml",
			"desenvolver-dominio/SKILL.md", "desenvolver-testes/SKILL.md", "projetar-banco/SKILL.md",
			"desenvolver-infraestrutura-dominio/SKILL.md", "desenvolver-backend/SKILL.md",
			"desenvolver-frontend/SKILL.md", "programar-defensivamente/SKILL.md");

	private static final List<String> SKILLS_FRAMEWORK = List.of("desenvolver-dominio-d5/SKILL.md",
			"desenvolver-infraestrutura-dominio-d5/SKILL.md", "desenvolver-backend-d5/SKILL.md");

	private final Configuration freemarker;

	public Gerador() {
		freemarker = new Configuration(Configuration.VERSION_2_3_34);
		freemarker.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
		freemarker.setDefaultEncoding(StandardCharsets.UTF_8.name());
		freemarker.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}

	public void gerar(File diretorio, String groupId, String nome, Set<String> subdominios, boolean framework) {
		gerar(diretorio.toPath(), groupId, nome, subdominios, framework);
	}

	public void gerar(Path diretorio, String groupId, String nome, Set<String> subdominios, boolean framework) {
		Objects.requireNonNull(diretorio);
		Objects.requireNonNull(groupId);
		Objects.requireNonNull(nome);
		Objects.requireNonNull(subdominios);
		if (subdominios.isEmpty()) {
			throw new IllegalArgumentException("É necessário informar ao menos um subdomínio");
		}

		var raiz = diretorio.resolve(nome);
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

		gerarAgentes(raiz, groupId, nome, subdominiosOrdenados, framework);

		try (var modulos = Files.list(raiz)) {
			for (var modulo : (Iterable<Path>) modulos::iterator) {
				if (Files.isDirectory(modulo) && !modulo.getFileName().toString().startsWith(".")) {
					criarDiretoriosPadrao(modulo);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Falha ao gerar " + raiz, e);
		}
	}

	private void gerarAgentes(Path raiz, String groupId, String nome, Set<String> subdominios, boolean framework) {
		var projeto = Map.of("name", nome, "groupId", groupId, "version", "1.0.0-SNAPSHOT", "framework", framework,
				"subdomains", subdominios, "mavenRootProject", Map.of("artifactId", nome + "-pai"));
		var modelo = Map.<String, Object>of("d5Project", projeto);

		if (framework) {
			copiarD5(raiz.resolve(".agents/d5"));
		}

		processar("AGENTS.md.ftlh", modelo, raiz.resolve("AGENTS.md"));

		var skills = new ArrayList<>(SKILLS);
		if (framework) {
			skills.addAll(SKILLS_FRAMEWORK);
		}
		for (var skill : skills) {
			processar(DIRETORIO_SKILLS + skill + ".ftlh", modelo, raiz.resolve(".agents/skills/" + skill));
		}
	}

	private void copiarD5(Path destino) {
		try {
			var resolvedor = new PathMatchingResourcePatternResolver();
			var raiz = resolvedor.getResource("classpath:d5/").getURI().getSchemeSpecificPart();
			for (var recurso : resolvedor.getResources("classpath*:d5/**")) {
				if (!recurso.isReadable()) {
					continue;
				}
				var caminho = recurso.getURI().getSchemeSpecificPart();
				if (!caminho.startsWith(raiz)) {
					throw new IllegalStateException("Recurso " + caminho + " fora de " + raiz);
				}
				var arquivo = destino.resolve(caminho.substring(raiz.length()));
				Files.createDirectories(arquivo.getParent());
				try (var entrada = recurso.getInputStream()) {
					Files.copy(entrada, arquivo, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Falha ao copiar o D5", e);
		}
	}

	private void gerarPomRaiz(Path raiz, String groupId, String nome, Set<String> subdominios, boolean framework) {
		processar("pom-raiz.ftl",
				Map.of("groupId", groupId, "nome", nome, "subdominios", subdominios, "framework", framework),
				raiz.resolve("pom.xml"));
	}

	private void gerarDominioCompartilhado(Path raiz, String groupId, String nome, String artifactId,
			boolean framework) {
		var modulo = raiz.resolve("dominio-compartilhado");

		gerarPomDominio(modulo, groupId, nome, artifactId, null, framework);

		var pacote = groupId + "." + nome + ".dominio";
		var mainJava = modulo.resolve("src/main/java/" + pacote.replace('.', '/'));
		processar("package-info.java.ftl", Map.of("pacote", pacote), mainJava.resolve("package-info.java"));
		processar("Autorizacao.java.ftl", Map.of("pacote", pacote), mainJava.resolve("Autorizacao.java"));
	}

	private void gerarDominio(Path raiz, String groupId, String nome, String subdominio, String artifactIdCompartilhado,
			boolean framework) {
		var modulo = raiz.resolve("dominio-" + subdominio);

		gerarPomDominio(modulo, groupId, nome, nome + "-dominio-" + subdominio, artifactIdCompartilhado, framework);

		var pacote = groupId + "." + nome + ".dominio." + subdominio;
		var testeJava = modulo.resolve("src/test/java/" + pacote.replace('.', '/'));
		processar("CucumberTests.java.ftl", Map.of("pacote", pacote), testeJava.resolve("CucumberTests.java"));
	}

	private void gerarPomDominio(Path modulo, String groupId, String nome, String artifactId,
			@Nullable String dominioCompartilhado, boolean framework) {
		var modelo = new HashMap<String, Object>();
		modelo.put("groupId", groupId);
		modelo.put("nome", nome);
		modelo.put("artifactId", artifactId);
		modelo.put("dominioCompartilhado", dominioCompartilhado);
		modelo.put("framework", framework);
		processar("pom-dominio.ftl", modelo, modulo.resolve("pom.xml"));
	}

	private void gerarAplicacao(Path raiz, String groupId, String nome, Set<String> subdominios, boolean framework) {
		processar("pom-aplicacao.ftl",
				Map.of("groupId", groupId, "nome", nome, "subdominios", subdominios, "framework", framework),
				raiz.resolve("aplicacao/pom.xml"));
	}

	private void gerarInfraestrutura(Path raiz, String groupId, String nome, boolean framework) {
		var modulo = raiz.resolve("infraestrutura");

		processar("pom-infraestrutura.ftl", Map.of("groupId", groupId, "nome", nome, "framework", framework),
				modulo.resolve("pom.xml"));

		var pacote = groupId + "." + nome + ".infraestrutura";
		var testeJava = modulo.resolve("src/test/java/" + pacote.replace('.', '/'));
		processar("InfraestruturaTesteAplicacao.java.ftl", Map.of("pacote", pacote),
				testeJava.resolve("InfraestruturaTesteAplicacao.java"));
	}

	private void gerarUiBackend(Path raiz, String groupId, String nome, boolean framework) {
		var modulo = raiz.resolve("ui-backend");

		processar("pom-ui-backend.ftl", Map.of("groupId", groupId, "nome", nome, "framework", framework),
				modulo.resolve("pom.xml"));

		var pacote = groupId + "." + nome;
		var raizJava = modulo.resolve("src/main/java/" + pacote.replace('.', '/'));

		processar("package-info.java.ftl", Map.of("pacote", pacote), raizJava.resolve("package-info.java"));
		processar("Aplicacao.java.ftl", Map.of("pacote", pacote), raizJava.resolve("BackendAplicacao.java"));

		if (framework) {
			var pacoteSeguranca = pacote + ".ui.backend.seguranca";
			var seguranca = raizJava.resolve("ui/backend/seguranca");

			processar("package-info.java.ftl", Map.of("pacote", pacoteSeguranca),
					seguranca.resolve("package-info.java"));
			processar("SegurancaConfiguracao.java.ftl", Map.of("pacote", pacote),
					seguranca.resolve("SegurancaConfiguracao.java"));
			processar("AutenticacaoControlador.java.ftl", Map.of("pacote", pacote),
					seguranca.resolve("AutenticacaoControlador.java"));
		}
	}

	private void processar(String template, Map<String, Object> modelo, Path destino) {
		try {
			Files.createDirectories(destino.getParent());

			var modeloTemplate = freemarker.getTemplate(template);
			try (var escritor = new OutputStreamWriter(Files.newOutputStream(destino), StandardCharsets.UTF_8)) {
				modeloTemplate.process(modelo, escritor);
			}
		} catch (IOException | TemplateException e) {
			throw new IllegalStateException("Falha ao gerar " + destino, e);
		}
	}

	private void criarDiretoriosPadrao(Path modulo) {
		for (var caminho : DIRETORIOS_PADRAO) {
			var diretorio = modulo.resolve(caminho);
			try {
				Files.createDirectories(diretorio);
				try (var conteudo = Files.list(diretorio)) {
					if (conteudo.findAny().isEmpty()) {
						Files.createFile(diretorio.resolve(".gitkeep"));
					}
				}
			} catch (IOException e) {
				throw new IllegalStateException("Falha ao gerar " + diretorio, e);
			}
		}
	}
}
