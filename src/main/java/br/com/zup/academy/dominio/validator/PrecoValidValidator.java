package br.com.zup.academy.dominio.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.zup.academy.dominio.validator.anotacao.PrecoValid;

public class PrecoValidValidator implements ConstraintValidator<PrecoValid, String>{

	private double min;
	private double max;

	@Override
	public void initialize(PrecoValid constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean valido = false;
		BigDecimal maxPreco = new BigDecimal(this.max);
		BigDecimal minPreco = new BigDecimal(this.min);
		BigDecimal preco = new BigDecimal(value);
		
		boolean ehMaior = preco.compareTo(minPreco) >= 0;
		boolean ehMenor = preco.compareTo(maxPreco) <= 0;
		valido = ehMaior && ehMenor;
		
//		Assert.state(preco.compareTo(minPreco) >= 0 && preco.compareTo(maxPreco) <= 0, "Preco informado é inválido");
		return valido;
	}
}