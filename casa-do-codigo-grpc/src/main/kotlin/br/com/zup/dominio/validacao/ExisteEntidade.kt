package br.com.zup.dominio.validacao

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [ExisteEntidadeValidator::class])
annotation class ExisteEntidade(
    val entidade: KClass<*>,
    val campo: String,
    val message: String = "O valor informado não existe. Por favor informe outro",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ExisteEntidadeValidator(
    private val manager: EntityManager,
) : ConstraintValidator<ExisteEntidade, Any> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun isValid(
        value: Any?,
        annotationMetadata: AnnotationValue<ExisteEntidade>,
        context: ConstraintValidatorContext,
    ): Boolean {
        this.logger.info("ExisteEntidade -> efetuando validação de existe entidade")

        if (value == null) {
            this.logger.info("ExisteEntidade -> valor informado é nulo")
            return false
        }

        val campo = annotationMetadata.get("campo", String::class.java).get()
        val entidade = annotationMetadata.get("entidade", Class::class.java).get().simpleName

        val jpql = "SELECT x FROM $entidade x WHERE x.$campo = :value"

        val ehValido = this.manager.createQuery(jpql)
            .setParameter("value", value)
            .resultList
            .isNotEmpty()

        if(ehValido) {
            this.logger.info("ExisteEntidade -> valor informado é válido")
            return true
        }

        this.logger.info("ExisteEntidade -> valor informado é inválido")
        return false
    }
}
