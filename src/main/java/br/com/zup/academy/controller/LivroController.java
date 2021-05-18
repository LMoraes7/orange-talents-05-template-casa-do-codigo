package br.com.zup.academy.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.academy.controller.form.LivroForm;
import br.com.zup.academy.dominio.modelo.Livro;
import br.com.zup.academy.dominio.repository.AutorRepository;
import br.com.zup.academy.dominio.repository.CategoriaRepository;
import br.com.zup.academy.dominio.repository.LivroRepository;

@RestController
@RequestMapping("/livros")
public class LivroController {

	private LivroRepository livroRepository;
	private CategoriaRepository categoriaRepository;
	private AutorRepository autorRepository;

	public LivroController(LivroRepository livroRepository, CategoriaRepository categoriaRepository,
			AutorRepository autorRepository) {
		this.livroRepository = livroRepository;
		this.categoriaRepository = categoriaRepository;
		this.autorRepository = autorRepository;
	}

	@PostMapping
	public ResponseEntity<Object> cadastrar(@RequestBody @Valid LivroForm livroForm) {
		Livro livro = livroForm.toLivro(this.categoriaRepository, this.autorRepository);
		System.out.println("kjlvhbfgdkjhdfkjghfdkjghbrtkjghb");
		this.livroRepository.save(livro);
		return ResponseEntity.ok().build();
	}
}
