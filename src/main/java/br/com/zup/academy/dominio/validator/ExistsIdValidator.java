package br.com.zup.academy.dominio.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.zup.academy.dominio.validator.anotacao.ExistsEntity;

public class ExistsIdValidator implements ConstraintValidator<ExistsEntity, Number>{

	private Class<?> domainClass;
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public void initialize(ExistsEntity constraintAnnotation) {
		domainClass = constraintAnnotation.domainClass();
	}
	
	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		Query query = this.manager.createQuery("SELECT 1 FROM "+domainClass.getName()+" WHERE id = :id")
				.setParameter("id", value);
		List<?> list = query.getResultList();
		boolean estaVazia = list.isEmpty();
//		false
//		Assert.state(list.size() >= 1, "Não existe um(a) " + domainClass + " com esse identificador");
		return !estaVazia; //true
	}
}
