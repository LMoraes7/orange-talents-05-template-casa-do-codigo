package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Categoria
import br.com.zup.dominio.repository.CategoriaRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.validation.Validator

@MicronautTest
internal class CategoriaRequestDtoTest(
    private val validator: Validator,
    private val repository: CategoriaRepository
) {

    lateinit var categoria: Categoria

    @BeforeEach
    internal fun setUp() {
        this.categoria =
            this.repository.save(this.criarCategoria(
                "categoria"
            ))
    }

    @AfterEach
    fun close() {
        this.repository.deleteAll()
    }

    @Test
    internal fun `deve validar`() {
        val errors = this.criarRequestDto("Esporte")
            .run {
                validar(this)
            }
        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `nome nao pode ser nulo e vazio`() {
        var errors = this.criarRequestDto(null)
            .run {
                validar(this)
            }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto("")
            .run {
                validar(this)
            }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nome nao pode ser duplicado`() {
        val errors = this.criarRequestDto(this.categoria.nome)
            .run {
                validar(this)
            }
        assertTrue(errors.isNotEmpty())
    }

    fun criarCategoria(nome: String) =
        Categoria(nome = nome)

    fun criarRequestDto(nome: String?) =
        CategoriaRequestDto(nome = nome)

    fun validar(request: CategoriaRequestDto) =
        this.validator.validate(request)
}