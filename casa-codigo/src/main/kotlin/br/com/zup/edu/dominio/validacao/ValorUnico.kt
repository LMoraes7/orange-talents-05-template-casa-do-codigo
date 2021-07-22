package br.com.zup.edu.dominio.validacao

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = [ValorUnicoValidator::class])
annotation class ValorUnico(
    val entidade: KClass<*>,
    val campo: String,
    val message: String = "Valor informado já está cadastrado. Por favor informe outro",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValorUnicoValidator(
    private val manager: EntityManager,
) : ConstraintValidator<ValorUnico, String> {

    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<ValorUnico>,
        context: ConstraintValidatorContext,
    ): Boolean {
        val campo = annotationMetadata.get("campo", String::class.java).get()
        val entidade = annotationMetadata.get("entidade", Class::class.java).get().simpleName

        val jpql = "SELECT x FROM $entidade x WHERE x.$campo = :value"

        val list = this.manager.createQuery(jpql)
            .setParameter("value", value)
            .resultList

        return list.isEmpty()
    }
}