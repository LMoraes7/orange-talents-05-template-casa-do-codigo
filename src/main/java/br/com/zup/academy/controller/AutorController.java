package br.com.zup.academy.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.academy.controller.form.AutorForm;
import br.com.zup.academy.dominio.modelo.Autor;
import br.com.zup.academy.dominio.repository.AutorRepository;
import br.com.zup.academy.dominio.validator.ProibeEmailDuplicadoAutorValidator;

@RestController
@RequestMapping("/autores")
public class AutorController {

	private AutorRepository autorRepository;
//	private ProibeEmailDuplicadoAutorValidator proibeEmailDuplicadoAutorValidator;

	public AutorController(AutorRepository autorRepository,
			ProibeEmailDuplicadoAutorValidator proibeEmailDuplicadoAutorValidator) {
		this.autorRepository = autorRepository;
//		this.proibeEmailDuplicadoAutorValidator = proibeEmailDuplicadoAutorValidator;
	}

//	Esse método irá executar a lógica de validações customizadas que foram passadas no .addValidators().
//	Esse método será executado de forma paralela a requisição feita pelo usuário
//	@InitBinder
//	public void init(WebDataBinder binder) {
//		binder.addValidators(proibeEmailDuplicadoAutorValidator);
//	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid AutorForm autorForm) {
		Autor autor = autorForm.toAutor();
		this.autorRepository.save(autor);
//		Minha ideia era retornar 201 com um AlunoDto no corpo da requisição, 
//		porém no desafio pedia para retornar 200 sem nada no corpo.
		return ResponseEntity.ok().build();
	}
}