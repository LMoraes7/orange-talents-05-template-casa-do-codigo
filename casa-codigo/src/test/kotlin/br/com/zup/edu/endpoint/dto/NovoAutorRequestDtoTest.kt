package br.com.zup.edu.endpoint.dto

import br.com.zup.edu.dominio.modelo.Autor
import br.com.zup.edu.dominio.repository.AutorRespository
import br.com.zup.edu.endpoint.autor.request.NovoAutorRequestDto
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.validation.Validator

@MicronautTest
internal class NovoAutorRequestDtoTest(
    private val validator: Validator,
    private val repository: AutorRespository
) {
    @Test
    internal fun `deve aceitar dados`() {
        val dto = NovoAutorRequestDto(
            nome = "Diego Castro",
            email = "diegocastro@email.com",
            descricao = "Descrição qualquer"
        )

        this.validator.validate(dto).run { assertTrue(this.isEmpty()) }
    }

    @Test
    internal fun `deve recusar o nome em branco e nulo`() {
        var dto = NovoAutorRequestDto(
            nome = "",
            email = "diegocastro@email.com",
            descricao = "Descrição qualquer"
        )

        this.validator.validate(dto).run { assertTrue(this.isNotEmpty()) }

        dto = NovoAutorRequestDto(
            nome = null,
            email = "diegocastroemail.com",
            descricao = "Descrição qualquer"
        )

        this.validator.validate(dto).run { assertTrue(this.isNotEmpty()) }
    }

    @Test
    internal fun `deve recusar o email em branco, invalido e duplicado`() {
        var dto = NovoAutorRequestDto(
            nome = "Diego Castro",
            email = "",
            descricao = "Descrição qualquer"
        )

        this.validator.validate(dto).run { assertTrue(this.isNotEmpty()) }

        dto = NovoAutorRequestDto(
            nome = "Diego Castro",
            email = "diegocastroemail.com",
            descricao = "Descrição qualquer"
        )

        this.validator.validate(dto).run { assertTrue(this.isNotEmpty()) }

        this.cadastrarAutor()

        dto = NovoAutorRequestDto(
            nome = "Diego Castro",
            email = "diegocastro@email.com",
            descricao = "Descrição qualquer"
        )

        this.validator.validate(dto).run { assertTrue(this.isNotEmpty()) }
    }

    private fun cadastrarAutor() {
        this.repository.save(
            Autor(
                nome = "Diego Castro",
                email = "diegocastro@email.com",
                descricao = "Descrição qualquer"
            )
        )
    }

    @Test
    internal fun `deve recusar descricao em branco, nula e acima de 400 caracteres`() {
        var dto = NovoAutorRequestDto(
            nome = "Diego Castro",
            email = "diegocastro@email.com",
            descricao = ""
        )

        this.validator.validate(dto).run { assertTrue(this.isNotEmpty()) }

        dto = NovoAutorRequestDto(
            nome = "Diego Castro",
            email = "diegocastro@email.com",
            descricao = null
        )

        this.validator.validate(dto).run { assertTrue(this.isNotEmpty()) }

        dto = NovoAutorRequestDto(
            nome = "Diego Castro",
            email = "diegocastro@email.com",
            descricao = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                    "\n" +
                    "Why do we use it?\n" +
                    "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)."
        )

        this.validator.validate(dto).run { assertTrue(this.isNotEmpty()) }
    }
}