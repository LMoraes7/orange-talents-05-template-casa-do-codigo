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

import br.com.zup.academy.controller.form.EstadoForm;
import br.com.zup.academy.dominio.modelo.Estado;
import br.com.zup.academy.dominio.repository.EstadoRepository;
import br.com.zup.academy.dominio.validator.ProibeNomeDuplicadoDoEstadoNoPais;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	private EstadoRepository estadoRepository;
	private EntityManager manager;
	private ProibeNomeDuplicadoDoEstadoNoPais proibeNomeDuplicadoNoPais;

	public EstadoController(EstadoRepository estadoRepository, EntityManager manager,
			ProibeNomeDuplicadoDoEstadoNoPais validator) {
		this.estadoRepository = estadoRepository;
		this.manager = manager;
		this.proibeNomeDuplicadoNoPais = validator;
	}
	
	@InitBinder
	public void init(WebDataBinder binder) {
		binder.addValidators(proibeNomeDuplicadoNoPais);
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody @Valid EstadoForm estadoForm) {
		Estado estado = estadoForm.toEstado(this.manager);
		this.estadoRepository.save(estado);
		return ResponseEntity.ok().build();
	}
}