package br.com.zup.academy.controller;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.academy.controller.form.LivroForm;
import br.com.zup.academy.dominio.modelo.Livro;
import br.com.zup.academy.dominio.repository.LivroRepository;

@RestController
@RequestMapping("/livros")
public class LivroController {

	private LivroRepository livroRepository;
	private EntityManager manager;

	public LivroController(LivroRepository livroRepository, EntityManager manager) {
		this.livroRepository = livroRepository;
		this.manager = manager;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<Object> cadastrar(@RequestBody @Valid LivroForm livroForm) {
		Livro livro = livroForm.toLivro(this.manager);
		this.livroRepository.save(livro);
		return ResponseEntity.ok().build();
	}
}
