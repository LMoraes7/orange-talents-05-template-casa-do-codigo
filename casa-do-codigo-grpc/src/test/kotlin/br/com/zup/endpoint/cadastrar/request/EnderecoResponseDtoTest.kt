package br.com.zup.endpoint.cadastrar.request

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.validation.Validator

@MicronautTest
internal class EnderecoResponseDtoTest(
    private val validator: Validator,
) {

    @Test
    internal fun `deve validar`() {
        val errors = this.criarRequestDto(
            "logradouro",
            "complemento",
            "cidade",
            "20725080"
        ).run { validar(this) }

        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `logradouro nao deve ser nulo e vazio`() {
        var errors = this.criarRequestDto(
            null,
            "complemento",
            "cidade",
            "20725080"
        ).run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto(
            "",
            "complemento",
            "cidade",
            "20725080"
        ).run { validar(this) }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `cidade nao deve ser nula e vazio`() {
        var errors = this.criarRequestDto(
            "logradouro",
            "complemento",
            null,
            "20725080"
        ).run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto(
            "logradouro",
            "complemento",
            "",
            "20725080"
        ).run { validar(this) }

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `cep nao deve ser nulo, vazio e invalido`() {
        var errors = this.criarRequestDto(
            "logradouro",
            "complemento",
            "cidade",
            null
        ).run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto(
            "logradouro",
            "complemento",
            "cidade",
            ""
        ).run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto(
            "logradouro",
            "complemento",
            "cidade",
            "207250800"
        ).run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto(
            "logradouro",
            "complemento",
            "cidade",
            "jkghf8667"
        ).run { validar(this) }
        assertTrue(errors.isNotEmpty())
    }

    fun validar(dto: EnderecoResponseDto) =
        this.validator.validate(dto)

    fun criarRequestDto(
        logradouro: String?,
        complemento: String?,
        cidade: String?,
        cep: String?,
    ) =
        EnderecoResponseDto(
            logradouro = logradouro,
            complemento = complemento,
            cidade = cidade,
            cep = cep
        )
}