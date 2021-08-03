package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Autor
import br.com.zup.dominio.modelo.Categoria
import br.com.zup.dominio.modelo.Livro
import br.com.zup.dominio.repository.AutorRepository
import br.com.zup.dominio.repository.CategoriaRepository
import br.com.zup.dominio.repository.LivroRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.Validator

@MicronautTest
internal class LivroRequestDtoTest(
    private val validator: Validator,
    private val autorRepository: AutorRepository,
    private val categoriaRepository: CategoriaRepository,
    private val livroRepository: LivroRepository,
) {

    lateinit var autor: Autor
    lateinit var categoria: Categoria
    lateinit var livro: Livro

    @BeforeEach
    internal fun setUp() {
        this.salvarAutor()
        this.salvarCategoria()
        this.salvarLivro()
    }

    private fun salvarLivro() {
        this.livroRepository.save(this.criarLivro(
            titulo = "tituloDoLivro",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            autor = this.autor,
            categoria = this.categoria
        )).also { this.livro = it }
    }

    private fun salvarCategoria() {
        this.categoriaRepository.save(this.criarCategoria(
            "categoria"
        )).also { this.categoria = it }
    }

    private fun salvarAutor() {
        this.autorRepository.save(this.criarAutor(
            "nome",
            "email@email.com",
            "descricao"
        )).also { this.autor = it }
    }

    @AfterEach
    fun close() {
        this.livroRepository.deleteAll()
        this.autorRepository.deleteAll()
        this.categoriaRepository.deleteAll()
    }

    @Test
    internal fun `deve validar`() {
        val errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("20.00"),
            paginas = 100,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }

        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `titulo nao deve ser nulo, vazio e duplicado`() {
        var errors = this.criarLivroRequest(
            titulo = null,
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = this.livro.titulo,
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `resumo nao deve ser nulo e vazio`() {
        var errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = null,
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `sumario nao deve ser nulo e vazio`() {
        var errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = null,
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `preco nao deve ser nulo e menor que 20`() {
        var errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = null,
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("19.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `paginas nao pode ser nula e menor que 100`() {
        var errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = null,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 99,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `isbn nao pode ser nulo e vazio`() {
        var errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = null,
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `dataPublicacao nao pode ser nula e no passado ou presente`() {
        var errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = null,
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now(),
            categoriaId = this.categoria.id,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `categoriaId nao pode ser nula e invalida`() {
        var errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = null,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = 10,
            autorId = this.autor.id
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `autorId nao pode ser nulo e invalido`() {
        var errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = null
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())

        errors = this.criarLivroRequest(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            preco = BigDecimal("50.00"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id,
            autorId = 10
        ).run {
            validar(this)
        }
        assertTrue(errors.isNotEmpty())
    }

    fun validar(dto: LivroRequestDto) =
        this.validator.validate(dto)

    fun criarLivroRequest(
        titulo: String?,
        resumo: String?,
        sumario: String?,
        preco: BigDecimal?,
        paginas: Int?,
        isbn: String?,
        dataPublicacao: LocalDate?,
        categoriaId: Long?,
        autorId: Long?,
    ) =
        LivroRequestDto(
            titulo = titulo,
            resumo = resumo,
            sumario = sumario,
            preco = preco,
            paginas = paginas,
            isbn = isbn,
            dataPublicacao = dataPublicacao,
            categoriaId = categoriaId,
            autorId = autorId
        )

    fun criarAutor(nome: String, email: String, descricao: String) =
        Autor(
            nome = nome,
            email = email,
            descricao = descricao
        )

    fun criarCategoria(nome: String) =
        Categoria(nome = nome)

    fun criarLivro(
        titulo: String,
        resumo: String,
        sumario: String,
        preco: BigDecimal,
        paginas: Int,
        isbn: String,
        dataPublicacao: LocalDate,
        autor: Autor,
        categoria: Categoria,
    ) =
        Livro(
            titulo = titulo,
            resumo = resumo,
            sumario = sumario,
            preco = preco,
            paginas = paginas,
            isbn = isbn,
            dataPublicacao = dataPublicacao,
            autor = autor,
            categoria = categoria
        )
}