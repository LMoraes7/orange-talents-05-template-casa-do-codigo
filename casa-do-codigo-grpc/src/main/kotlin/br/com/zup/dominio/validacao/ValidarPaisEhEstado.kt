package br.com.zup.dominio.validacao

import br.com.zup.endpoint.cadastrar.request.RegiaoResponseDto
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
@Target(CLASS)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidarPaisEhEstadoValidator::class])
annotation class ValidarPaisEhEstado(
    val mensagem: String = "Requisição inválida",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValidarPaisEhEstadoValidator(
    private val manager: EntityManager,
) : ConstraintValidator<ValidarPaisEhEstado, RegiaoResponseDto> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun isValid(
        value: RegiaoResponseDto,
        annotationMetadata: AnnotationValue<ValidarPaisEhEstado>,
        context: ConstraintValidatorContext,
    ): Boolean {
        this.logger.info("ValidarPaisEhEstado -> efetuando validação de país e estado")

        if (value.paisId == null || value.estadoId == null) {
            this.logger.info("ValidarPaisEhEstado -> valores informados são nulos")
            return false
        }

        if (!existeEstadoNoPais(value.estadoId, value.paisId, this.manager)) {
            this.logger.info("ValidarPaisEhEstado -> não existe o estado informado no país informado")
            return false
        }

        this.logger.info("ValidarPaisEhEstado -> validação válida")
        return true
    }

    fun existeEstadoNoPais(estadoId: Long, paisId: Long, manager: EntityManager) =
        manager.createQuery("SELECT e FROM Estado e WHERE e.id = :estadoId AND e.pais.id = :paisId")
            .setParameter("estadoId", estadoId)
            .setParameter("paisId", paisId)
            .resultList
            .isNotEmpty()
}
