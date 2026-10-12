package com.example.exemplo.infraestrutura.geografia.municipio;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.exemplo.aplicacao.geografia.municipio.MunicipioProjecao;

public interface IMunicipioRepositorioJpa extends JpaRepository<MunicipioJpa, String> {
	@Query("""
			select count(m) > 0
			  from MunicipioJpa m
			 where lower(m.nome) = lower(:nome)
			   and m.unidadeFederativa = :unidadeFederativa
			   and (:identificadorIgnorado is null or m.identificador <> :identificadorIgnorado)
			""")
	boolean existePorNome(String nome, String unidadeFederativa, @Nullable String identificadorIgnorado);

	@Query("""
			select count(c) > 0
			  from ClienteJpa c
			 where c.municipio = :identificador
			""")
	boolean existeReferencia(String identificador);

	@Query("""
			select new com.example.exemplo.aplicacao.geografia.municipio.MunicipioProjecao(m.identificador, m.nome, m.unidadeFederativa, u.nome, m.extinto)
			  from MunicipioJpa m
			  join UnidadeFederativaJpa u on u.identificador = m.unidadeFederativa
			 where (:nome is null or lower(m.nome) like :nome escape '\\')
			   and (:unidadeFederativa is null or m.unidadeFederativa = :unidadeFederativa)
			""")
	Page<MunicipioProjecao> pesquisar(@Nullable String nome, @Nullable String unidadeFederativa, Pageable pageable);
}