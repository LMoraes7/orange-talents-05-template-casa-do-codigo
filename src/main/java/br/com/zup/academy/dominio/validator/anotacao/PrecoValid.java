package br.com.zup.academy.dominio.validator.anotacao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;

import br.com.zup.academy.dominio.validator.PrecoValidValidator;

@Documented
@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD })
@Constraint(validatedBy = { PrecoValidValidator.class })
@Retention(RetentionPolicy.RUNTIME)
@NotBlank
public @interface PrecoValid {
	
	String message() default "Preço deve ser válido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	double min() default 0;
	
	double max() default Integer.MAX_VALUE;
}