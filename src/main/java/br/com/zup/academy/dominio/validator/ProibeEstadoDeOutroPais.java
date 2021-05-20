package br.com.zup.academy.dominio.validator;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.zup.academy.controller.form.ClienteForm;
import br.com.zup.academy.dominio.util.VerificadorPaisEstado;

@Component
public class ProibeEstadoDeOutroPais implements Validator {

	private EntityManager manager;

	public ProibeEstadoDeOutroPais(EntityManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ClienteForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors())
			return;
		
		ClienteForm clienteForm = (ClienteForm) target;
		
		boolean paisPossuiEstados = VerificadorPaisEstado.paisPossuiEstados(manager, clienteForm.getPaisId());
		boolean estadoEstaPresente = clienteForm.estadoEstaPresente();
		
		if(!paisPossuiEstados && estadoEstaPresente) {
			errors.rejectValue("estadoId", null, "O País informado não possui Estados!");
			return;
		}
		if(paisPossuiEstados && !estadoEstaPresente) {
			errors.rejectValue("estadoId", null, "O País informado possui Estados!");
			return;
		}
		if(!paisPossuiEstados) 
			return;
		if(!VerificadorPaisEstado.paisPossuiDeterminadoEstado(manager, clienteForm.getPaisId(), clienteForm.getEstadoId())) 
			errors.rejectValue("estadoId", null, "O Estado informado ou não existe ou não faz parte do País informado!");
	}
}