package br.com.zup.academy.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.academy.controller.dto.LivroDetalhadoDto;
import br.com.zup.academy.controller.dto.LivrosDto;
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
	
	@GetMapping
	public ResponseEntity<List<LivrosDto>> listar() {
		List<Livro> livros = this.livroRepository.findAll();
		List<LivrosDto> livrosDto =  LivrosDto.toLivrosDto(livros);
		return ResponseEntity.ok(livrosDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<LivroDetalhadoDto> listarPorId(@PathVariable("id") Long id) {
		Optional<Livro> livroOptional =  this.livroRepository.consultarPorId(id);
		if(livroOptional.isPresent()) {
			Livro livro = livroOptional.get();
			LivroDetalhadoDto livroDto = new LivroDetalhadoDto(livro);
			return ResponseEntity.ok(livroDto);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@Transactional
	public ResponseEntity<Object> cadastrar(@RequestBody @Valid LivroForm livroForm) {
		Livro livro = livroForm.toLivro(this.manager);
		this.livroRepository.save(livro);
		return ResponseEntity.ok().build();
	}
}