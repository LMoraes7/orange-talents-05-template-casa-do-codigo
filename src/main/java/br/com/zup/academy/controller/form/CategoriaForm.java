package br.com.zup.academy.controller.form;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.zup.academy.dominio.modelo.Categoria;
import br.com.zup.academy.dominio.validator.anotacao.UniqueValue;

public class CategoriaForm {

	@NotBlank
	@UniqueValue(fieldName = "nome", domainClass = Categoria.class, message = "A Categoria informada já consta no cadastro")
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