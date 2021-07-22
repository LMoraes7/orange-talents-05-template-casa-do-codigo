package br.com.zup.edu.endpoint.categoria.request

import br.com.zup.edu.dominio.modelo.Categoria
import br.com.zup.edu.dominio.repository.CategoriaRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.validation.Validator

@MicronautTest
internal class NovaCategoriaRequestDtoTest(
    private val repository: CategoriaRepository,
    private val validator: Validator
) {

    @Test
    internal fun `deve validar dados`() {
        val dto = NovaCategoriaRequestDto(nome = "Esportes")

        val errors = this.validator.validate(dto)

        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `nao deve validar nome em branco, nulo e duplicado`() {
        var dto = NovaCategoriaRequestDto(nome = "")

        var errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = NovaCategoriaRequestDto(nome = null)

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        this.cadastrarCategoria()

        dto = NovaCategoriaRequestDto(nome = "Esportes")

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    private fun cadastrarCategoria() {
        this.repository.save(
            Categoria(nome = "Esportes")
        )
    }
}