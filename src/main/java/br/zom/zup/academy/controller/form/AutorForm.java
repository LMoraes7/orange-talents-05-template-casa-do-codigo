package br.zom.zup.academy.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.zom.zup.academy.dominio.modelo.Autor;
import br.zom.zup.academy.dominio.repository.AutorRepository;
import br.zom.zup.academy.exception.ValidacaoException;

public class AutorForm {

	@NotBlank
	private String nome;
	@NotBlank
	@Email
	private String email;
	@NotBlank
	@Size(max = 400)
	private String descricao;

	public AutorForm(@NotBlank String nome, @NotBlank @Email String email,
			@NotBlank @Size(max = 400) String descricao) {
		this.nome = nome;
		this.email = email;
		this.descricao = descricao;
	}

	public Autor toAutor(AutorRepository autorRepository) {
		if(!emailJaEstaCadastrado(autorRepository))
			return new Autor(this.nome, this.email, this.descricao);
		throw new ValidacaoException("Email informado já foi cadastrado!");
	}

	private boolean emailJaEstaCadastrado(AutorRepository autorRepository) {
		return autorRepository.findByEmail(this.email).isPresent();
	}
}