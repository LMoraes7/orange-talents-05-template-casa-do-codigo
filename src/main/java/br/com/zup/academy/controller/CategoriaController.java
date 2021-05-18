package br.com.zup.academy.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.academy.controller.form.CategoriaForm;
import br.com.zup.academy.dominio.modelo.Categoria;
import br.com.zup.academy.dominio.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	private CategoriaRepository categoriaRepository;
//	private ProibeNomeDuplicadoCategoriaValidator proibeNomeDuplicadoCategoriaValidator;

	public CategoriaController(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
//		this.proibeNomeDuplicadoCategoriaValidator = proibeNomeDuplicadoCategoriaValidator;
	}

//	@InitBinder
//	public void init(WebDataBinder binder) {
//		binder.addValidators(proibeNomeDuplicadoCategoriaValidator);
//	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid CategoriaForm categoriaForm) {
		Categoria categoria = categoriaForm.toCategoria();
		this.categoriaRepository.save(categoria);
		return ResponseEntity.ok().build();
	}
}