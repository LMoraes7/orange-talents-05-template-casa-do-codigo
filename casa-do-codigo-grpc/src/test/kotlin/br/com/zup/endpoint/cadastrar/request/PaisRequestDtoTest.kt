package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Pais
import br.com.zup.dominio.repository.PaisRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.validation.Validator

@MicronautTest
internal class PaisRequestDtoTest(
    private val validator: Validator,
    private val repository: PaisRepository,
) {

    lateinit var pais: Pais

    @BeforeEach
    internal fun setUp() {
        this.salvarPais("Argentina")
            .also { this.pais = it }
    }

    fun salvarPais(nome: String) =
        this.repository.save(this.criarPais(nome))

    fun criarPais(nome: String) =
        Pais(nome = nome)

    @AfterEach
    fun close() {
        this.repository.deleteAll()
    }

    @Test
    internal fun `deve validar`() {
        val errors = this.criarRequestDto("Brasil")
            .run { validar(this) }
        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `nome nao deve ser nulo, vazio e duplicado`() {
        var errors = this.criarRequestDto(null)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto("")
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto(this.pais.nome)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())
    }

    fun validar(dto: PaisRequestDto) =
        this.validator.validate(dto)

    fun criarRequestDto(nome: String?) =
        PaisRequestDto(nome = nome)
}