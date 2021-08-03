package br.com.zup.dominio.validacao

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [TelefoneValidator::class])
annotation class Telefone(
    val mensagem: String = "Telefone informado não é válido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class TelefoneValidator: ConstraintValidator<Telefone, String> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<Telefone>,
        context: ConstraintValidatorContext,
    ): Boolean {
        this.logger.info("Telefone -> efetuando validação de telefone")

        if(value.isNullOrBlank()) {
            this.logger.info("Telefone -> valor informado é nulo ou vazio")
            return false
        }

        val ehValido = value.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())

        if(ehValido) {
            this.logger.info("Telefone -> valor informado é válido")
            return true
        }

        this.logger.info("Telefone -> valor informado não é válido")
        return false
    }
}
