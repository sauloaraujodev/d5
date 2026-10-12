package com.example.exemplo.infraestrutura.crm.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.exemplo.dominio.crm.cliente.Cliente;
import com.example.exemplo.dominio.crm.cliente.ClienteCpf;
import com.example.exemplo.dominio.crm.cliente.ClienteIdentificador;
import com.example.exemplo.dominio.crm.cliente.Endereco;
import com.example.exemplo.dominio.crm.cliente.EnderecoCep;
import com.example.exemplo.dominio.geografia.municipio.MunicipioCodigoIbge;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;

@DataJpaTest
class ClienteRepositorioTest {
	private static final MunicipioCodigoIbge SAO_PAULO = new MunicipioCodigoIbge("3550308");
	private static final ClienteCpf CPF_MARIA = new ClienteCpf("11111111111");
	private static final ClienteCpf CPF_ANA = new ClienteCpf("22222222222");

	@Autowired
	private IClienteRepositorioJpa repositorioJpa;

	private ClienteRepositorio repositorio;

	@BeforeEach
	void configurar() {
		repositorio = new ClienteRepositorio(repositorioJpa);
	}

	private static Endereco criarEndereco(Optional<String> complemento) {
		return new Endereco("Avenida Paulista", "1000", complemento, "Bela Vista", new EnderecoCep("01310100"),
				SAO_PAULO);
	}

	private static Cliente criar(ClienteCpf cpf, String nome) {
		return criar(cpf, nome, Optional.empty());
	}

	private static Cliente criar(ClienteCpf cpf, String nome, Optional<String> complemento) {
		return new Cliente(Optional.empty(), cpf, nome, criarEndereco(complemento));
	}

	@Test
	void incluiGerandoIdentificador() {
		var cliente = repositorio.incluir(criar(CPF_MARIA, "Maria", Optional.of("Sala 12")));

		var identificador = cliente.getIdentificador().orElseThrow();
		var buscado = repositorio.buscarPorIdentificador(identificador);

		assertEquals(CPF_MARIA, buscado.getCpf());
		assertEquals("Maria", buscado.getNome());
		assertEquals("Sala 12", buscado.getEndereco().getComplemento().orElseThrow());
		assertEquals(SAO_PAULO, buscado.getEndereco().getMunicipio());
		assertEquals(new EnderecoCep("01310100"), buscado.getEndereco().getCep());
	}

	@Test
	void incluiSemComplemento() {
		var cliente = repositorio.incluir(criar(CPF_MARIA, "Maria"));

		var buscado = repositorio.buscarPorIdentificador(cliente.getIdentificador().orElseThrow());

		assertTrue(buscado.getEndereco().getComplemento().isEmpty());
	}

	@Test
	void buscaPorIdentificadorInexistenteLancaExcecao() {
		assertThrows(AgregadoInexistenteExcecao.class,
				() -> repositorio.buscarPorIdentificador(new ClienteIdentificador("999")));
	}

	@Test
	void existePorIdentificador() {
		var cliente = repositorio.incluir(criar(CPF_MARIA, "Maria"));

		assertTrue(repositorio.existePorIdentificador(cliente.getIdentificador().orElseThrow()));
		assertFalse(repositorio.existePorIdentificador(new ClienteIdentificador("999")));
	}

	@Test
	void altera() {
		var identificador = repositorio.incluir(criar(CPF_MARIA, "Maria")).getIdentificador();

		repositorio.alterar(new Cliente(identificador, CPF_MARIA, "Maria Silva", criarEndereco(Optional.empty())));

		assertEquals("Maria Silva", repositorio.buscarPorIdentificador(identificador.orElseThrow()).getNome());
	}

	@Test
	void exclui() {
		var identificador = repositorio.incluir(criar(CPF_MARIA, "Maria")).getIdentificador().orElseThrow();

		repositorio.excluir(identificador);

		assertFalse(repositorio.existePorIdentificador(identificador));
	}

	@Test
	void existePorCpfConsiderandoTodosOsClientes() {
		repositorio.incluir(criar(CPF_MARIA, "Maria"));

		assertTrue(repositorio.existePorCpf(CPF_MARIA, Optional.empty()));
		assertFalse(repositorio.existePorCpf(CPF_ANA, Optional.empty()));
	}

	@Test
	void existePorCpfIgnorandoOProprioIdentificador() {
		var identificador = repositorio.incluir(criar(CPF_MARIA, "Maria")).getIdentificador();

		assertFalse(repositorio.existePorCpf(CPF_MARIA, identificador));
	}

	@Test
	void existePorCpfNaoIgnorandoUmClienteDiferente() {
		repositorio.incluir(criar(CPF_MARIA, "Maria"));
		var identificador = repositorio.incluir(criar(CPF_ANA, "Ana")).getIdentificador();

		assertTrue(repositorio.existePorCpf(CPF_MARIA, identificador));
	}
}
