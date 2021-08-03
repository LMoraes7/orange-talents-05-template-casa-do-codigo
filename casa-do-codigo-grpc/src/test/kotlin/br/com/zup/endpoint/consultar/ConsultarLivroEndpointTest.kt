package br.com.zup.endpoint.consultar

import br.com.zup.ConsultarLivroRequestGrpc
import br.com.zup.ConsultarLivroServiceGrpc
import br.com.zup.dominio.modelo.Autor
import br.com.zup.dominio.modelo.Categoria
import br.com.zup.dominio.modelo.Livro
import br.com.zup.dominio.repository.AutorRepository
import br.com.zup.dominio.repository.CategoriaRepository
import br.com.zup.dominio.repository.LivroRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class ConsultarLivroEndpointTest(
    private val grpcClient: ConsultarLivroServiceGrpc.ConsultarLivroServiceBlockingStub,
    private val autorRepository: AutorRepository,
    private val categoriaRepository: CategoriaRepository,
    private val livroRepository: LivroRepository,
) {

    lateinit var autor: Autor
    lateinit var categoria: Categoria
    lateinit var livro: Livro

    @BeforeEach
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
    internal fun `deve consultar`() {
        val responseGrpc = this.grpcClient.consultar(
            ConsultarLivroRequestGrpc.newBuilder()
                .setId(this.livro.id!!)
                .build()
        )

        with(responseGrpc) {
            assertNotNull(this.titulo)
            assertEquals(livro.titulo, this.titulo)
        }
    }

    @Test
    internal fun `deve retornar NOT_FOUND`() {
        val error = assertThrows<StatusRuntimeException> {
            this.grpcClient.consultar(
                ConsultarLivroRequestGrpc.newBuilder()
                    .setId(10)
                    .build()
            )
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, this.status.code)
        }
    }

    @Factory
    class ConsultarLivro {

        @Singleton
        fun consultar(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                ConsultarLivroServiceGrpc.ConsultarLivroServiceBlockingStub =
            ConsultarLivroServiceGrpc.newBlockingStub(channel)
    }
}