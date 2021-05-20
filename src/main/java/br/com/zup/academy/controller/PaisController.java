package br.com.zup.academy.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.academy.controller.form.PaisForm;
import br.com.zup.academy.dominio.modelo.Pais;
import br.com.zup.academy.dominio.repository.PaisRepository;

@RestController
@RequestMapping("/paises")
public class PaisController {

	private PaisRepository paisRepository;

	public PaisController(PaisRepository paisRepository) {
		this.paisRepository = paisRepository;
	}
	
	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody @Valid PaisForm paisForm) {
		Pais pais = paisForm.toPais();
		this.paisRepository.save(pais);
		return ResponseEntity.ok().build();
	}
}
