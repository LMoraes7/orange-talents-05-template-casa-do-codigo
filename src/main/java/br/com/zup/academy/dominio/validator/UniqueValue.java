package br.com.zup.academy.dominio.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
// Informa a classe que implementa a validação da regra de negócio
@Constraint(validatedBy = {UniqueValueValidator.class})
// Informa os locais onde essa anotação pode ser usada
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValue {

	String message() default "{javax.validation.constraints.UniqueValue.message}";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default{}; 
	
	String fieldName();
	
	Class<?> domainClass();
}
