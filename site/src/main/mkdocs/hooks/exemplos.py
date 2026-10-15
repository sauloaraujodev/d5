"""Verifica a coerência entre as versões do projeto de exemplo usadas nas lições do tutorial.

Cada lição inclui, via snippets, os arquivos do projeto ao final dela: exemplo1, exemplo2 e exemplo.
Um arquivo presente em duas versões consecutivas deve ser idêntico nelas, exceto os que a lição
seguinte altera de propósito, listados em ALTERACOES. Assim, uma correção feita em apenas uma das
versões interrompe o build da documentação, em vez de deixar as lições divergirem.
"""

import logging
from pathlib import Path

from mkdocs.exceptions import PluginError

log = logging.getLogger("mkdocs.hooks.exemplos")

VERSOES = ("exemplo1", "exemplo2", "exemplo")

_GEOGRAFIA_DOMINIO = "dominio-geografia/src/main/java/com/example/exemplo/dominio/geografia"
_GEOGRAFIA_DOMINIO_TESTE = "dominio-geografia/src/test/java/com/example/exemplo/dominio/geografia"
_GEOGRAFIA_FEATURES = "dominio-geografia/src/test/resources/features"
_GEOGRAFIA_INFRAESTRUTURA = "infraestrutura/src/main/java/com/example/exemplo/infraestrutura/geografia"
_GEOGRAFIA_INFRAESTRUTURA_TESTE = "infraestrutura/src/test/java/com/example/exemplo/infraestrutura/geografia"

# Arquivos que cada lição altera em relação à anterior, relativos à raiz de cada versão.
ALTERACOES = {
    # Lição 2: a unidade federativa passa a ser referenciada pelo município.
    ("exemplo1", "exemplo2"): {
        f"{_GEOGRAFIA_DOMINIO}/unidadefederativa/spi/IUnidadeFederativaRepositorio.java",
        f"{_GEOGRAFIA_DOMINIO}/unidadefederativa/UnidadeFederativaServico.java",
        f"{_GEOGRAFIA_DOMINIO_TESTE}/unidadefederativa/UnidadeFederativaStepDefinitions.java",
        f"{_GEOGRAFIA_FEATURES}/unidade-federativa.feature",
        f"{_GEOGRAFIA_INFRAESTRUTURA}/unidadefederativa/IUnidadeFederativaRepositorioJpa.java",
        f"{_GEOGRAFIA_INFRAESTRUTURA}/unidadefederativa/UnidadeFederativaRepositorio.java",
        f"{_GEOGRAFIA_INFRAESTRUTURA_TESTE}/unidadefederativa/UnidadeFederativaRepositorioTest.java",
    },
    # Lição 3: o módulo dominio-crm passa a depender de dominio-geografia e o município passa a ser
    # referenciado pelo cliente.
    ("exemplo2", "exemplo"): {
        "dominio-crm/pom.xml",
        f"{_GEOGRAFIA_DOMINIO}/municipio/spi/IMunicipioRepositorio.java",
        f"{_GEOGRAFIA_DOMINIO}/municipio/MunicipioServico.java",
        f"{_GEOGRAFIA_DOMINIO_TESTE}/municipio/MunicipioStepDefinitions.java",
        f"{_GEOGRAFIA_FEATURES}/municipio.feature",
        f"{_GEOGRAFIA_INFRAESTRUTURA}/municipio/IMunicipioRepositorioJpa.java",
        f"{_GEOGRAFIA_INFRAESTRUTURA}/municipio/MunicipioRepositorio.java",
        f"{_GEOGRAFIA_INFRAESTRUTURA_TESTE}/municipio/MunicipioRepositorioTest.java",
    },
}


def _arquivos(versao: Path) -> set[str]:
    """Devolve os arquivos-fonte de uma versão: os pom.xml e o conteúdo de src de cada módulo."""
    arquivos = set()
    if (versao / "pom.xml").is_file():
        arquivos.add("pom.xml")
    for modulo in versao.iterdir():
        if (modulo / "pom.xml").is_file():
            arquivos.add(f"{modulo.name}/pom.xml")
        if (modulo / "src").is_dir():
            arquivos.update(f.relative_to(versao).as_posix() for f in (modulo / "src").rglob("*") if f.is_file())
    return arquivos


def _conteudo(arquivo: Path) -> bytes:
    # Ignora a diferença entre CRLF e LF, que depende da configuração do Git de cada checkout.
    return arquivo.read_bytes().replace(b"\r\n", b"\n")


def _problemas(raiz: Path, anterior: str, seguinte: str) -> list[str]:
    arquivos_anterior = _arquivos(raiz / anterior)
    arquivos_seguinte = _arquivos(raiz / seguinte)
    comuns = arquivos_anterior & arquivos_seguinte
    alteracoes = ALTERACOES[(anterior, seguinte)]

    problemas = [f"{anterior}/{caminho} não existe em {seguinte}"
                 for caminho in sorted(arquivos_anterior - arquivos_seguinte)]
    problemas += [f"{caminho}, listado como alterado de {anterior} para {seguinte}, não existe em ambas"
                  for caminho in sorted(alteracoes - comuns)]
    for caminho in sorted(comuns):
        iguais = _conteudo(raiz / anterior / caminho) == _conteudo(raiz / seguinte / caminho)
        if caminho in alteracoes and iguais:
            problemas.append(f"{caminho}, listado como alterado de {anterior} para {seguinte}, é igual em ambas")
        elif caminho not in alteracoes and not iguais:
            problemas.append(f"{seguinte}/{caminho} difere de {anterior}/{caminho}")
    return problemas


def on_config(config):
    raiz = Path(config.config_file_path).resolve().parents[4]
    problemas = []
    for anterior, seguinte in zip(VERSOES, VERSOES[1:]):
        problemas += _problemas(raiz, anterior, seguinte)
    if problemas:
        raise PluginError(
            "As versões do projeto de exemplo divergem. Replique a mudança nas demais versões ou, se a "
            "lição altera o arquivo de propósito, registre-o em ALTERACOES, em site/src/main/mkdocs/hooks/"
            "exemplos.py:\n" + "\n".join(f"  - {problema}" for problema in problemas))
    log.info("Versões do projeto de exemplo coerentes: %s", ", ".join(VERSOES))
    return config
