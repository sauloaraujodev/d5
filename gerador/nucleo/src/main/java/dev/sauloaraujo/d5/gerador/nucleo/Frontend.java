package dev.sauloaraujo.d5.gerador.nucleo;

import java.util.Arrays;
import java.util.Locale;

/**
 * A tecnologia do módulo ui-frontend. Cada uma tem seus templates e o diretório
 * do módulo onde a aplicação é gerada.
 */
public enum Frontend {
	ANGULAR("ui-frontend/", "src/main/angular"), REACT("ui-frontend-react/", "src/main/react");

	private final String diretorioTemplates;
	private final String diretorioAplicacao;

	Frontend(String diretorioTemplates, String diretorioAplicacao) {
		this.diretorioTemplates = diretorioTemplates;
		this.diretorioAplicacao = diretorioAplicacao;
	}

	public String getDiretorioTemplates() {
		return diretorioTemplates;
	}

	public String getDiretorioAplicacao() {
		return diretorioAplicacao;
	}

	public static Frontend de(String nome) {
		return Arrays.stream(values()).filter(frontend -> frontend.name().equals(nome.toUpperCase(Locale.ROOT)))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Frontend desconhecido: " + nome
						+ ". Os valores possíveis são " + String.join(", ", Arrays.stream(values())
								.map(frontend -> frontend.name().toLowerCase(Locale.ROOT)).toList())));
	}
}
