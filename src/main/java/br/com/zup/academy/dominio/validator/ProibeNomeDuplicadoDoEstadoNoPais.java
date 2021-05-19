package br.com.zup.academy.dominio.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.zup.academy.controller.form.EstadoForm;
import br.com.zup.academy.dominio.modelo.Estado;
import br.com.zup.academy.dominio.repository.EstadoRepository;

@Component
public class ProibeNomeDuplicadoDoEstadoNoPais implements Validator {

	private EstadoRepository estadoRepository;

	public ProibeNomeDuplicadoDoEstadoNoPais(EstadoRepository estadoRepository) {
		this.estadoRepository = estadoRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return EstadoForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors())
			return;
		EstadoForm estadoForm = (EstadoForm) target;
		Optional<Estado> estadoFormOptional = this.estadoRepository.findByNomeAndPaisId(estadoForm.getNome(), estadoForm.getPaisId());
		if(estadoFormOptional.isPresent())
			errors.rejectValue("nome", null, "Estado "+estadoForm.getNome()+ " já foi cadastrado para o País de ID " +estadoForm.getPaisId());
	}
}