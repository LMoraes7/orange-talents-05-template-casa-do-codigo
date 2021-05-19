package br.com.zup.academy.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.zup.academy.dominio.modelo.Livro;

public class LivrosDto {

	public static List<LivrosDto> toLivrosDto(List<Livro> livros) {
		return livros.stream().map(LivrosDto::new).collect(Collectors.toList());
	}

	private Long id;
	private String titulo;

	public LivrosDto(Livro livro) {
		this.id = livro.getId();
		this.titulo = livro.getTitulo();
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

}