package br.zom.zup.academy.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.zom.zup.academy.controller.form.AutorForm;
import br.zom.zup.academy.dominio.modelo.Autor;
import br.zom.zup.academy.dominio.repository.AutorRepository;

@RestController
@RequestMapping("/autores")
public class AutorController {

	private AutorRepository autorRepository;

	public AutorController(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody @Valid AutorForm autorForm) {
		Autor autor = autorForm.toAutor();
		this.autorRepository.save(autor);
//		Minha ideia era retornar 201 com um AlunoDto no corpo da requisição, 
//		porém no desafio pedia para retornar 200 sem nada no corpo.
		return ResponseEntity.ok().build();
	}
}