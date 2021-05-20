package br.com.zup.academy.controller.form;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.zup.academy.dominio.modelo.Cliente;
import br.com.zup.academy.dominio.modelo.Estado;
import br.com.zup.academy.dominio.modelo.Pais;
import br.com.zup.academy.dominio.validator.anotacao.CPForCNPJ;
import br.com.zup.academy.dominio.validator.anotacao.ExistsId;
import br.com.zup.academy.dominio.validator.anotacao.UniqueValue;

public class ClienteForm {

	@NotNull
	@Email
	@UniqueValue(domainClass = Cliente.class, fieldName = "email")
	private String email;
	@NotBlank
	private String nome;
	@NotBlank
	private String sobrenome;
	@NotBlank
	@CPForCNPJ
	@UniqueValue(domainClass = Cliente.class, fieldName = "documento")
	private String documento;
	@NotBlank
	private String endereco;
	@NotBlank
	private String complemento;
	@NotBlank
	private String cidade;
	@NotBlank
	private String telefone;
	@NotBlank
	private String cep;
	@NotNull
	@ExistsId(domainClass = Pais.class, field = "id")
	private Long paisId;
	private Long estadoId;

	@JsonCreator
	public ClienteForm(@NotNull @Email String email, @NotBlank String nome, @NotBlank String sobrenome,
			@NotBlank String documento, @NotBlank String endereco, @NotBlank String complemento,
			@NotBlank String cidade, @NotBlank String telefone, @NotBlank String cep, @NotNull Long paisId,
			Long estadoId) {
		this.email = email;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.documento = documento;
		this.endereco = endereco;
		this.complemento = complemento;
		this.cidade = cidade;
		this.telefone = telefone;
		this.cep = cep;
		this.paisId = paisId;
		this.estadoId = estadoId;
	}

	public Long getPaisId() {
		return paisId;
	}

	public Long getEstadoId() {
		return estadoId;
	}

	public boolean estadoEstaPresente() {
		return Optional.ofNullable(this.estadoId).isPresent();
	}

	public Cliente toCliente(EntityManager manager) {
		Pais pais = manager.find(Pais.class, this.paisId);
		Cliente cliente = new Cliente(email, nome, sobrenome, documento, endereco, complemento, cidade, pais, telefone,
				cep);
		if (this.estadoEstaPresente()) {
			Estado estado = manager.find(Estado.class, this.estadoId);
			cliente.setEstado(estado);
		}
		return cliente;
	}
}