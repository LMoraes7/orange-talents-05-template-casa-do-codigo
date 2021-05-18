package br.com.zup.academy.dominio.validator.anotacao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

import br.com.zup.academy.dominio.validator.ExistsIdValidator;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
@Constraint(validatedBy = {ExistsIdValidator.class})
@NotNull
public @interface ExistsEntity {

	String message() default "Não existe uma entidade com esse identificador";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default{}; 
	
	Class<?> domainClass();
}
