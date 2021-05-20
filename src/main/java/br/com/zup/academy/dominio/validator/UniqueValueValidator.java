package br.com.zup.academy.dominio.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.Assert;

import br.com.zup.academy.dominio.validator.anotacao.UniqueValue;

//No Generics, informamos a anotação que iremos tratar e o que iremos receber para efetuar o tratamento da validação
public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

	private String campo;
	private Class<?> classe;

	@PersistenceContext
	private EntityManager manager;

	@Override
	public void initialize(UniqueValue constraintAnnotation) {
		campo = constraintAnnotation.fieldName();
		classe = constraintAnnotation.domainClass();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Query query = this.manager
				.createQuery("SELECT 1 FROM " + classe.getName() + " WHERE " + campo + " = :value")
				.setParameter("value", value);
		List<?> list = query.getResultList();
		Assert.state(list.size() <= 1,
				"Foi encontrado mais de um " + classe + " com o atributo " + campo + " = " + value);
		return list.isEmpty();
	}
}