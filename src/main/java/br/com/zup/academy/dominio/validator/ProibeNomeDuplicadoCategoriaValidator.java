package br.com.zup.academy.dominio.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.zup.academy.controller.form.CategoriaForm;
import br.com.zup.academy.dominio.modelo.Categoria;
import br.com.zup.academy.dominio.repository.CategoriaRepository;

@Component
public class ProibeNomeDuplicadoCategoriaValidator implements Validator {

	private CategoriaRepository categoriaRepository;

	public ProibeNomeDuplicadoCategoriaValidator(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return CategoriaForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors())
			return;
		CategoriaForm categoriaForm = (CategoriaForm) target;
		Optional<Categoria> categoriaOptional = categoriaRepository.findByNome(categoriaForm.getNome());
		if(categoriaOptional.isPresent())
			errors.rejectValue("nome", null, "Categoria -> "+categoriaForm.getNome()+" já foi cadastrado(a)!");
	}
}