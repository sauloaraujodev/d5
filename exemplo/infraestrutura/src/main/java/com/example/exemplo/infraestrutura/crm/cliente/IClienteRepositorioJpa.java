package com.example.exemplo.infraestrutura.crm.cliente;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.exemplo.aplicacao.crm.cliente.ClienteProjecao;

public interface IClienteRepositorioJpa extends JpaRepository<ClienteJpa, Long> {
	@Query("""
			select count(c) > 0
			  from ClienteJpa c
			 where c.cpf = :cpf
			   and (:identificadorIgnorado is null or c.identificador <> :identificadorIgnorado)
			""")
	boolean existePorCpf(String cpf, @Nullable Long identificadorIgnorado);

	@Query("""
			select new com.example.exemplo.aplicacao.crm.cliente.ClienteProjecao(c.identificador, c.nome, c.municipio, m.nome, m.unidadeFederativa, u.nome)
			  from ClienteJpa c
			  join MunicipioJpa m on m.identificador = c.municipio
			  join UnidadeFederativaJpa u on u.identificador = m.unidadeFederativa
			 where (:nome is null or lower(c.nome) like :nome escape '\\')
			   and (:municipio is null or c.municipio = :municipio)
			   and (:unidadeFederativa is null or m.unidadeFederativa = :unidadeFederativa)
			""")
	Page<ClienteProjecao> pesquisar(@Nullable String nome, @Nullable String municipio,
			@Nullable String unidadeFederativa, Pageable pageable);
}
