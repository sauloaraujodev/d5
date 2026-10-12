package com.example.exemplo.dominio.geografia.municipio;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.example.exemplo.dominio.Autorizacao;
import com.example.exemplo.dominio.geografia.municipio.spi.IMunicipioRepositorio;
import com.example.exemplo.dominio.geografia.unidadefederativa.spi.IUnidadeFederativaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoServicoAbstrato;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;

public final class MunicipioServico
		extends AgregadoServicoAbstrato<MunicipioCodigoIbge, Municipio, MunicipioAutorizacoes> {
	public static final String IDENTIFICADOR_INEXISTENTE_MODELO = "Não existe município com o identificador %s";
	public static final String IDENTIFICADOR_EXISTENTE_MODELO = "Já existe um município com o identificador %s";
	public static final String NOME_EXISTENTE_MODELO = "Já existe um município com o nome %s na unidade federativa";
	public static final String UNIDADE_FEDERATIVA_INEXISTENTE_MODELO = "Não existe unidade federativa com o identificador %s";
	public static final String EXTINCAO_INDEVIDA = "O município só pode ser extinto pela ação de extinção";
	public static final String EXTINTO_MODELO = "O município %s está extinto";

	private final IMunicipioRepositorio repositorio;
	private final IUnidadeFederativaRepositorio unidadeFederativaRepositorio;

	public MunicipioServico(ISegurancaServico segurancaServico, IMunicipioRepositorio repositorio,
			IUnidadeFederativaRepositorio unidadeFederativaRepositorio) {
		super(segurancaServico, repositorio, IDENTIFICADOR_EXISTENTE_MODELO, IDENTIFICADOR_INEXISTENTE_MODELO);

		this.repositorio = Objects.requireNonNull(repositorio);
		this.unidadeFederativaRepositorio = Objects.requireNonNull(unidadeFederativaRepositorio);
	}

	@Override
	public MunicipioAutorizacoes criarAutorizacoes(Optional<MunicipioCodigoIbge> identificador) {
		Objects.requireNonNull(identificador);

		var administrador = getSegurancaServico().possuiAutorizacao(Autorizacao.ADMINISTRADOR.name());
		return new MunicipioAutorizacoes(administrador, true, administrador, administrador, administrador);
	}

	@Override
	protected void validarInclusaoAlteracao(Municipio agregado) {
		Objects.requireNonNull(agregado);

		Validate.isTrue(!agregado.isExtinto(), EXTINCAO_INDEVIDA);

		validarExistencia(unidadeFederativaRepositorio::existePorIdentificador, agregado.getUnidadeFederativa(),
				UNIDADE_FEDERATIVA_INEXISTENTE_MODELO);

		validarInexistencia(
				nome -> repositorio.existePorNome(nome, agregado.getUnidadeFederativa(), agregado.getIdentificador()),
				agregado.getNome(), NOME_EXISTENTE_MODELO);
	}

	@Override
	protected void validarAlteracao(Municipio agregado) {
		Objects.requireNonNull(agregado);

		var identificador = agregado.getIdentificador().orElseThrow();
		validarNaoExtinto(repositorio.buscarPorIdentificador(identificador));
	}

	public void extinguir(MunicipioCodigoIbge identificador) {
		Objects.requireNonNull(identificador);

		validarAutorizacao(Optional.of(identificador), MunicipioAutorizacoes::getExtinguir);
		validarExistencia(identificador);

		var municipio = repositorio.buscarPorIdentificador(identificador);
		validarNaoExtinto(municipio);
		repositorio.alterar(municipio.extinguir());
	}

	private void validarNaoExtinto(Municipio municipio) {
		if (municipio.isExtinto()) {
			throw new IllegalStateException(String.format(EXTINTO_MODELO, municipio.getIdentificador().orElseThrow()));
		}
	}
}