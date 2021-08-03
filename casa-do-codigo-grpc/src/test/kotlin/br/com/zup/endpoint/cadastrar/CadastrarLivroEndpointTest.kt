package br.com.zup.endpoint.cadastrar

import br.com.zup.BDecimal
import br.com.zup.CadastrarLivroServiceGrpc
import br.com.zup.NovoLivroRequestGrpc
import br.com.zup.dominio.modelo.Autor
import br.com.zup.dominio.modelo.Categoria
import br.com.zup.dominio.modelo.Livro
import br.com.zup.dominio.repository.AutorRepository
import br.com.zup.dominio.repository.CategoriaRepository
import br.com.zup.dominio.repository.LivroRepository
import com.google.protobuf.ByteString
import com.google.protobuf.Timestamp
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
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class CadastrarLivroEndpointTest(
    private val grpcClient: CadastrarLivroServiceGrpc.CadastrarLivroServiceBlockingStub,
    private val autorRepository: AutorRepository,
    private val categoriaRepository: CategoriaRepository,
    private val livroRepository: LivroRepository,
) {

    lateinit var autor: Autor
    lateinit var categoria: Categoria

    @BeforeEach
    internal fun setUp() {
        this.salvarAutor()
        this.salvarCategoria()
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

    @Test
    internal fun `deve cadastrar`() {
        val responseGrpc = this.criarRequestGrpc(
            titulo = "titulo",
            resumo = "resumo",
            sumario = "sumario",
            scale = 2,
            valor = BigInteger("5000"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id!!,
            autorId = this.autor.id!!
        ).run {
            grpcClient.cadastrar(this)
        }

        with(responseGrpc) {
            assertNotNull(this.id)
        }
    }

    @Test
    internal fun `deve retornar INVALID_ARGUMENT`() {
        val requestGrpc = this.criarRequestGrpc(
            titulo = "",
            resumo = "resumo",
            sumario = "sumario",
            scale = 2,
            valor = BigInteger("5000"),
            paginas = 150,
            isbn = "isbn",
            dataPublicacao = LocalDate.now().plusDays(1),
            categoriaId = this.categoria.id!!,
            autorId = this.autor.id!!
        )

        val error = assertThrows<StatusRuntimeException> { this.grpcClient.cadastrar(requestGrpc) }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, this.status.code)
        }
    }

    fun criarRequestGrpc(
        titulo: String,
        resumo: String,
        sumario: String,
        scale: Int,
        valor: BigInteger,
        paginas: Int,
        isbn: String,
        dataPublicacao: LocalDate,
        categoriaId: Long,
        autorId: Long,
    ): NovoLivroRequestGrpc {
        val instant = dataPublicacao.atStartOfDay(ZoneId.systemDefault()).toInstant()

        return NovoLivroRequestGrpc.newBuilder()
            .setTitulo(titulo)
            .setResumo(resumo)
            .setSumario(sumario)
            .setPreco(BDecimal.newBuilder()
                .setScale(scale)
                .setValorInt(BDecimal.BInteger.newBuilder()
                    .setValorBytes(ByteString.copyFrom(valor.toByteArray()))
                    .build())
                .build())
            .setPaginas(paginas)
            .setIsbn(isbn)
            .setDataPublicacao(Timestamp.newBuilder()
                .setSeconds(instant.epochSecond)
                .setNanos(instant.nano)
                .build())
            .setCategoriaId(categoriaId)
            .setAutorId(autorId)
            .build()
    }

    @Factory
    class CadastrarLivro {

        @Singleton
        fun cadastrar(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                CadastrarLivroServiceGrpc.CadastrarLivroServiceBlockingStub =
            CadastrarLivroServiceGrpc.newBlockingStub(channel)
    }
}