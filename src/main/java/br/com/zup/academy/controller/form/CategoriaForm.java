package br.com.zup.academy.controller.form;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.zup.academy.dominio.modelo.Categoria;

public class CategoriaForm {

	@NotBlank
	private String nome;
	
	@JsonCreator
	public CategoriaForm(@NotBlank String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	public Categoria toCategoria() {
		return new Categoria(this.nome);
	}
}