package br.com.zup.dominio.validacao

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidarDocumentoValidator::class])
annotation class ValidarDocumento(
    val message: String = "Documento não é nem um CPF e nem um CNPJ",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValidarDocumentoValidator: ConstraintValidator<ValidarDocumento, String> {
    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<ValidarDocumento>,
        context: ConstraintValidatorContext,
    ): Boolean {
        if(value.isNullOrBlank())
            return false

        val ehUmCpf = CPFValidator().run {
            initialize(null)
            isValid(value, null)
        }

        val ehUmCnpj = CNPJValidator().run {
            initialize(null)
            isValid(value, null)
        }

        if(ehUmCpf || ehUmCnpj)
            return true
        return false
    }

}
