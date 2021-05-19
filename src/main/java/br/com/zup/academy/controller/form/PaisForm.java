package br.com.zup.academy.controller.form;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.zup.academy.dominio.modelo.Pais;
import br.com.zup.academy.dominio.validator.anotacao.UniqueValue;

public class PaisForm {

	@NotBlank
	@UniqueValue(domainClass = Pais.class, fieldName = "nome")
	private String nome;
	
	@JsonCreator
	public PaisForm(@NotBlank String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Pais toPais() {
		return new Pais(this.nome);
	}
}