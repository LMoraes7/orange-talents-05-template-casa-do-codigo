package br.com.zup.edu.dominio.validacao

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [ExisteEntidadeValidator::class])
annotation class ExisteEntidade(
    val entidade: KClass<*>,
    val campo: String,
    val message: String = "Valor informado não existe. Por favor informe outro",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ExisteEntidadeValidator(
    private val manager: EntityManager
): ConstraintValidator<ExisteEntidade, Any> {

    override fun isValid(
        value: Any?,
        annotationMetadata: AnnotationValue<ExisteEntidade>,
        context: ConstraintValidatorContext,
    ): Boolean {
        val campo = annotationMetadata.get("campo", String::class.java).get()
        val entidade = annotationMetadata.get("entidade", Class::class.java).get().simpleName

        val jpql = "SELECT x FROM $entidade x WHERE x.$campo = :value"

        val list = this.manager.createQuery(jpql)
            .setParameter("value", value)
            .resultList

        return list.isNotEmpty()
    }

}
