package br.com.zup.academy.dominio.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.zup.academy.dominio.validator.anotacao.DataFutureValid;

public class DataFutureValidValidator implements ConstraintValidator<DataFutureValid, Object>{
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		LocalDate parse = LocalDate.parse((String) value);
		LocalDate now = LocalDate.now();
//		Assert.state(parse.isAfter(now), "Data informada deve ser posterior ao dia de hoje");
		return parse.isAfter(now);
	}
}