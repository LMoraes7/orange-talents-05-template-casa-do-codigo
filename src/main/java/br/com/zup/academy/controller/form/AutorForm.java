package br.com.zup.academy.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.zup.academy.dominio.modelo.Autor;
import br.com.zup.academy.dominio.validator.UniqueValue;

public class AutorForm {

	@NotBlank
	private String nome;
	@NotBlank
	@Email
	@UniqueValue(fieldName = "email", domainClass = Autor.class, message = "O Email informado já consta no cadastro")
	private String email;
	@NotBlank
	@Size(max = 400)
	private String descricao;

	@JsonCreator
	public AutorForm(@NotBlank String nome, @NotBlank @Email String email,
			@NotBlank @Size(max = 400) String descricao) {
		this.nome = nome;
		this.email = email;
		this.descricao = descricao;
	}
	
	public String getEmail() {
		return email;
	}

	public Autor toAutor() {
		return new Autor(this.nome, this.email, this.descricao);
	}
}