package br.com.zup.academy.dominio.validator.anotacao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;

import br.com.zup.academy.dominio.validator.DataFutureValidValidator;

@Documented
@Constraint(validatedBy = {DataFutureValidValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
@NotBlank
public @interface DataFutureValid {
	
	String message() default "A data informada precisa ser posterior a data de hoje";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}