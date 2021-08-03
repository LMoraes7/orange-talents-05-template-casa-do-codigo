package br.com.zup.endpoint.consultar

import br.com.zup.ConsultarTodosLivrosRequestGrpc
import br.com.zup.ConsultarTodosLivrosServiceGrpc
import br.com.zup.dominio.modelo.Autor
import br.com.zup.dominio.modelo.Categoria
import br.com.zup.dominio.modelo.Livro
import br.com.zup.dominio.repository.AutorRepository
import br.com.zup.dominio.repository.CategoriaRepository
import br.com.zup.dominio.repository.LivroRepository
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class ConsultarTodosOsLivrosEndpointTest(
    private val grpcClient: ConsultarTodosLivrosServiceGrpc.ConsultarTodosLivrosServiceBlockingStub,
    private val autorRepository: AutorRepository,
    private val categoriaRepository: CategoriaRepository,
    private val livroRepository: LivroRepository,
) {

    lateinit var autor: Autor
    lateinit var categoria: Categoria
    lateinit var livro: Livro

    fun setUp() {
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
            categoria = this.categoria,
            autor = this.autor

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

    @Test
    internal fun `deve retornar 1 livro`() {
        this.setUp()

        val responseGrpc = this.grpcClient.consultar(ConsultarTodosLivrosRequestGrpc.newBuilder().build())

        with(responseGrpc) {
            assertNotNull(this.livrosList)
            assertEquals(1, this.livrosCount)
        }
    }

    @Test
    internal fun `deve retornar nenhum livro`() {
        val responseGrpc = this.grpcClient.consultar(ConsultarTodosLivrosRequestGrpc.newBuilder().build())

        with(responseGrpc) {
            assertNotNull(this.livrosList)
            assertEquals(0, this.livrosCount)
        }
    }

    @Factory
    class ConsultarLivros {

        @Singleton
        fun consultar(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                ConsultarTodosLivrosServiceGrpc.ConsultarTodosLivrosServiceBlockingStub =
            ConsultarTodosLivrosServiceGrpc.newBlockingStub(channel)
    }
}