package br.zom.zup.academy.exception;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.zom.zup.academy.controller.form.AutorForm;
import br.zom.zup.academy.dominio.modelo.Autor;
import br.zom.zup.academy.dominio.repository.AutorRepository;

@Component
public class ProibeEmailDuplicadoAutorValidator implements Validator {

	private AutorRepository autorRepository;

	public ProibeEmailDuplicadoAutorValidator(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}

//	Método para verificar se a classe que está chegando como argumento, 
//		é uma classe do mesmo tipo ou é um filho de AutorForm.
	@Override
	public boolean supports(Class<?> clazz) {
		return AutorForm.class.isAssignableFrom(clazz);
	}

//	Caso o método acima retorne TRUE, o método abaixo será chamado.
//	Esse método contêm a lógica de validação
	@Override
	public void validate(Object target, Errors errors) {
		if (errors.hasErrors())
			return;
		AutorForm autorForm = (AutorForm) target;
		Optional<Autor> autorOptional = this.autorRepository.findByEmail(autorForm.getEmail());
		if(autorOptional.isPresent()) 
			errors.rejectValue("email", null, "Email "+autorForm.getEmail()+" já foi cadastrado!");
	}
}