package br.com.zup.edu.endpoint.livro.request

import br.com.zup.edu.dominio.modelo.Autor
import br.com.zup.edu.dominio.modelo.Categoria
import br.com.zup.edu.dominio.repository.AutorRespository
import br.com.zup.edu.dominio.repository.CategoriaRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import javax.validation.Validator

@MicronautTest
internal class NovoLivroRequestDtoTest(
    private val validator: Validator,
    private val categoriaRepository: CategoriaRepository,
    private val autorRespository: AutorRespository,
) {

    lateinit var categoria: Categoria
    lateinit var autor: Autor

    @BeforeEach
    internal fun init() {
        this.categoria = this.categoriaRepository.save(
            this.criarCategoria("esportes")
        )

        this.autor = this.autorRespository.save(
            this.criarAutor(
                "email@email.com",
                "nome",
                "descricao"
            )
        )
    }

    @AfterEach
    fun close() {
        this.autorRespository.deleteAll()
        this.categoriaRepository.deleteAll()
    }

    @Test
    internal fun `deve validar`() {
        val dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        val errors = this.validator.validate(dto)

        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `nao deve validar quando titulo for nulo ou vazio`() {
        var dto = this.criarRequestDto(
            titulo = null,
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        var errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = this.criarRequestDto(
            titulo = "",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando resumo for nulo ou vazio`() {
        var dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = null,
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        var errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando sumario for nulo ou vazio`() {
        var dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = null,
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        var errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando precoScale for nulo`() {
        val dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = null,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        val errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando precoValor for nulo`() {
        val dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = null,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        val errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando paginas for nulo`() {
        val dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = null,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        val errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando isbn for nulo ou vazio`() {
        var dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = null,
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        var errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando dataPublicacao for no passado`() {
        val dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().minusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        )

        val errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando categoriaId for nula, vazia e invalida`() {
        var dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = null,
            autorId = this.autor.id
        )

        var errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = "",
            autorId = this.autor.id
        )

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = "categoriaId",
            autorId = this.autor.id
        )

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nao deve validar quando autorId for nula, vazia e invalida`() {
        var dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = null
        )

        var errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = ""
        )

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())

        dto = this.criarRequestDto(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isnb",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = "autorId"
        )

        errors = this.validator.validate(dto)

        assertTrue(errors.isNotEmpty())
    }

    private fun criarRequestDto(
        titulo: String?,
        resumo: String?,
        sumario: String?,
        precoScale: Int?,
        precoValor: Long?,
        paginas: Int?,
        isbn: String?,
        dataPublicacao: LocalDate,
        categoriaId: String?,
        autorId: String?
    ) =
        NovoLivroRequestDto(
            titulo = titulo,
            resumo = resumo,
            sumario = sumario,
            precoScale = precoScale,
            precoValor = precoValor,
            paginas = paginas,
            isbn = isbn,
            dataPublicacao = dataPublicacao,
            categoriaId = categoriaId,
            autorId = autorId
        )

    private fun criarCategoria(nome: String) =
        Categoria(
            nome = nome
        )

    private fun criarAutor(email: String, nome: String, descricao: String) =
        Autor(
            email = email,
            nome = nome,
            descricao = descricao
        )
}