package br.com.zup.academy.dominio.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.zup.academy.dominio.validator.anotacao.ExistsId;

public class ExistsIdValidator implements ConstraintValidator<ExistsId, Object>{

	private Class<?> classe;
	private String campo;
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public void initialize(ExistsId constraintAnnotation) {
		classe = constraintAnnotation.domainClass();
		campo = constraintAnnotation.field();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Query query = this.manager.createQuery("SELECT 1 FROM "+this.classe.getName()+" WHERE "+this.campo+" = :id")
			.setParameter("id", value);
		List<?> list = query.getResultList();
		return !(list.isEmpty());
	}
}
