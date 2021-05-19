package br.com.zup.academy.controller.form;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zup.academy.dominio.modelo.Estado;
import br.com.zup.academy.dominio.modelo.Pais;
import br.com.zup.academy.dominio.validator.anotacao.ExistsId;

public class EstadoForm {

	@NotNull
	@ExistsId(domainClass = Pais.class, field = "id")
	private Long paisId;
	@NotBlank
	private String nome;

	public EstadoForm(@NotNull Long paisId, @NotBlank String nome) {
		this.paisId = paisId;
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public Long getPaisId() {
		return paisId;
	}

	public Estado toEstado(EntityManager manager) {
		Pais pais = manager.find(Pais.class, this.paisId);
		return new Estado(this.nome, pais);
	}
}