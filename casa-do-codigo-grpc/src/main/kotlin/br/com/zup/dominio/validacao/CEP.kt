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
@Constraint(validatedBy = [CEPValidator::class])
@Target(FIELD)
@Retention(RUNTIME)
annotation class CEP(
    val mensagem: String = "CEP informado não é válido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class CEPValidator: ConstraintValidator<CEP, String> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<CEP>,
        context: ConstraintValidatorContext,
    ): Boolean {
        this.logger.info("CEP -> efetuando validação de cep")

        if(value.isNullOrBlank()) {
            this.logger.info("CEP -> valor informado é nulo ou vazio")
            return false
        }

        val ehValido = value.matches("[0-9]{5}[0-9]{3}".toRegex())

        if (ehValido) {
            this.logger.info("CEP -> valor informado é válido")
            return true
        }

        this.logger.info("CEP -> valor informado não é válido")
        return false
    }
}

