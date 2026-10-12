package com.example.exemplo.infraestrutura.geografia.municipio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.exemplo.dominio.geografia.municipio.Municipio;
import com.example.exemplo.dominio.geografia.municipio.MunicipioCodigoIbge;
import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativaCodigoIbge;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;

@DataJpaTest
class MunicipioRepositorioTest {
	private static final UnidadeFederativaCodigoIbge SAO_PAULO = new UnidadeFederativaCodigoIbge("35");
	private static final UnidadeFederativaCodigoIbge RIO_DE_JANEIRO = new UnidadeFederativaCodigoIbge("33");

	@Autowired
	private IMunicipioRepositorioJpa repositorioJpa;

	private MunicipioRepositorio repositorio;

	@BeforeEach
	void configurar() {
		repositorio = new MunicipioRepositorio(repositorioJpa);
	}

	private static Municipio criar(String codigoIbge, String nome) {
		return new Municipio(new MunicipioCodigoIbge(codigoIbge), nome);
	}

	@Test
	void incluiEBuscaPorIdentificador() {
		repositorio.incluir(criar("3550308", "São Paulo"));

		var municipio = repositorio.buscarPorIdentificador(new MunicipioCodigoIbge("3550308"));

		assertEquals("São Paulo", municipio.getNome());
		assertEquals(SAO_PAULO, municipio.getUnidadeFederativa());
		assertFalse(municipio.isExtinto());
	}

	@Test
	void buscaPorIdentificadorInexistenteLancaExcecao() {
		assertThrows(AgregadoInexistenteExcecao.class,
				() -> repositorio.buscarPorIdentificador(new MunicipioCodigoIbge("3550308")));
	}

	@Test
	void existePorIdentificador() {
		repositorio.incluir(criar("3550308", "São Paulo"));

		assertTrue(repositorio.existePorIdentificador(new MunicipioCodigoIbge("3550308")));
		assertFalse(repositorio.existePorIdentificador(new MunicipioCodigoIbge("3304557")));
	}

	@Test
	void alteraPreservandoExtincao() {
		repositorio.incluir(criar("3550308", "São Paulo").extinguir());

		repositorio.alterar(Municipio.extinto(new MunicipioCodigoIbge("3550308"), "São Paulo Alterado"));

		var municipio = repositorio.buscarPorIdentificador(new MunicipioCodigoIbge("3550308"));
		assertEquals("São Paulo Alterado", municipio.getNome());
		assertTrue(municipio.isExtinto());
	}

	@Test
	void exclui() {
		repositorio.incluir(criar("3550308", "São Paulo"));

		repositorio.excluir(new MunicipioCodigoIbge("3550308"));

		assertFalse(repositorio.existePorIdentificador(new MunicipioCodigoIbge("3550308")));
	}

	@Test
	void existePorNomeNaMesmaUnidadeFederativa() {
		repositorio.incluir(criar("3550308", "São Paulo"));

		assertTrue(repositorio.existePorNome("São Paulo", SAO_PAULO, Optional.empty()));
		assertFalse(repositorio.existePorNome("São Paulo", RIO_DE_JANEIRO, Optional.empty()));
		assertFalse(repositorio.existePorNome("Santos", SAO_PAULO, Optional.empty()));
	}

	@Test
	void existePorNomeIgnorandoOProprioIdentificador() {
		repositorio.incluir(criar("3550308", "São Paulo"));

		var identificador = Optional.of(new MunicipioCodigoIbge("3550308"));

		assertFalse(repositorio.existePorNome("São Paulo", SAO_PAULO, identificador));
	}

	@Test
	void existePorNomeNaoIgnorandoUmMunicipioDiferente() {
		repositorio.incluir(criar("3550308", "São Paulo"));
		repositorio.incluir(criar("3548500", "Santos"));

		var identificador = Optional.of(new MunicipioCodigoIbge("3548500"));

		assertTrue(repositorio.existePorNome("São Paulo", SAO_PAULO, identificador));
	}
}
