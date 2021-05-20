package br.com.zup.academy.controller;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.academy.controller.form.ClienteForm;
import br.com.zup.academy.dominio.modelo.Cliente;
import br.com.zup.academy.dominio.repository.ClienteRepository;
import br.com.zup.academy.dominio.validator.ProibeEstadoDeOutroPais;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private ClienteRepository clienteRepository;
	private ProibeEstadoDeOutroPais proibeEstadoDeOutroPais;
	private EntityManager manager;

	public ClienteController(ClienteRepository clienteRepository, ProibeEstadoDeOutroPais proibeEstadoDeOutroPais,
			EntityManager manager) {
		this.clienteRepository = clienteRepository;
		this.proibeEstadoDeOutroPais = proibeEstadoDeOutroPais;
		this.manager = manager;
	}

	@InitBinder
	public void init(WebDataBinder binder) {
		binder.addValidators(proibeEstadoDeOutroPais);
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody @Valid ClienteForm clienteForm) {
		Cliente cliente = clienteForm.toCliente(this.manager);
		this.clienteRepository.save(cliente);
		return ResponseEntity.ok().body("Cliente ID " + cliente.getId());
	}
}