package br.com.zup.dominio.validacao

import br.com.zup.dominio.modelo.Pais
import br.com.zup.endpoint.cadastrar.request.EstadoRequestDto
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidarNomeEstadoValidator::class])
annotation class ValidarNomeEstado(
    val mensagem: String = "Esatdo informado já está cadastrado",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValidarNomeEstadoValidator(
    private val manager: EntityManager,
) : ConstraintValidator<ValidarNomeEstado, EstadoRequestDto> {

    override fun isValid(
        value: EstadoRequestDto,
        annotationMetadata: AnnotationValue<ValidarNomeEstado>,
        context: ConstraintValidatorContext,
    ): Boolean {
        if (value.nome.isNullOrBlank() || value.paisId == null)
            return false

        if (!paisExiste(value.paisId, manager) || paisPossuiEstadoInformado(value.paisId, value.nome, manager))
            return false

        return true
    }

    fun paisExiste(paisId: Long, manager: EntityManager) =
        manager.find(Pais::class.java, paisId)
            .run { this != null }

    fun paisPossuiEstadoInformado(paisId: Long, nomeEstado: String, manager: EntityManager) =
        manager
            .createQuery(
                "SELECT p FROM Pais p JOIN FETCH p.estados e WHERE p.id = :paisId AND e.nome = :nomeEstado"
            )
            .setParameter("paisId", paisId)
            .setParameter("nomeEstado", nomeEstado)
            .resultList.isNotEmpty()
}