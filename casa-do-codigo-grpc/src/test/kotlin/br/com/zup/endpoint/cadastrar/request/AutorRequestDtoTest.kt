package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Autor
import br.com.zup.dominio.repository.AutorRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.validation.Validator

@MicronautTest
internal class AutorRequestDtoTest(
    private val validator: Validator,
    private val respository: AutorRepository,
) {

    lateinit var autor: Autor

    @BeforeEach
    internal fun setUp() {
        this.autor =
            this.respository.save(this.criarAutor(
                "nome",
                "email@email.com",
                "descricao"
            ))
    }

    @AfterEach
    fun close() {
        this.respository.deleteAll()
    }

    @Test
    internal fun `deve validar`() {
        val errors = this.criarAutorRequest(
            "nome",
            "email@emaill.com",
            "descrição"
        ).run {
            validar(this)
        }

        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `nome nao deve ser nulo ou vazio`() {
        var errors = this.criarAutorRequest(
            null,
            "email@email.com",
            "descrição"
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarAutorRequest(
            "",
            "email@email.com",
            "descrição"
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `email nao deve ser nulo, vazio ou invalido`() {
        var errors = this.criarAutorRequest(
            "nome",
            null,
            "descrição"
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarAutorRequest(
            "nome",
            "",
            "descrição"
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarAutorRequest(
            "nome",
            "email",
            "descrição"
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `email nao deve ser duplicado`() {
        val errors = this.criarAutorRequest(
            this.autor.nome,
            this.autor.email,
            this.autor.descricao
        ).run {
            validar(this)
        }

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `descricao nao deve ser nula, vazio ou maior do que 400 caracteres`() {
        var errors = this.criarAutorRequest(
            "nome",
            "email@email.com",
            null
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarAutorRequest(
            "nome",
            "email@email.com",
            ""
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarAutorRequest(
            "nome",
            "email@email.com",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                    "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    fun criarAutor(
        nome: String,
        email: String,
        descricao: String,
    ) =
        Autor(
            nome = nome,
            email = email,
            descricao = descricao
        )

    fun validar(request: AutorRequestDto) =
        this.validator.validate(request)

    fun criarAutorRequest(
        nome: String?,
        email: String?,
        descricao: String?,
    ) =
        AutorRequestDto(
            nome = nome,
            email = email,
            descricao = descricao
        )
}