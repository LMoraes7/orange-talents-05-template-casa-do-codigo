package br.com.zup.edu.endpoint.livro

import br.com.zup.edu.BigDecimal
import br.com.zup.edu.CadastrarLivroRequestGrpc
import br.com.zup.edu.CadastrarNovoLivroServiceGrpc
import br.com.zup.edu.dominio.modelo.Autor
import br.com.zup.edu.dominio.modelo.Categoria
import br.com.zup.edu.dominio.repository.AutorRespository
import br.com.zup.edu.dominio.repository.CategoriaRepository
import br.com.zup.edu.dominio.repository.LivroRepository
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
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class CadastrarLivroEnpointTest(
    private val grpcClient: CadastrarNovoLivroServiceGrpc.CadastrarNovoLivroServiceBlockingStub,
    private val categoriaRepository: CategoriaRepository,
    private val livroRepository: LivroRepository,
    private val autorRepository: AutorRespository
) {

    lateinit var categoria: Categoria
    lateinit var autor: Autor

    @BeforeEach
    internal fun init() {
        this.categoria = this.categoriaRepository.save(
            this.criarCategoria("esportes")
        )

        this.autor = this.autorRepository.save(
            this.criarAutor(
                "email@email.com",
                "nome",
                "descricao"
            )
        )
    }

    @AfterEach
    fun close() {
        this.livroRepository.deleteAll()
        this.categoriaRepository.deleteAll()
        this.autorRepository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar livro`() {
        val requestGrpc = this.criarRequestGrpc(
            data = LocalDate.now().plusDays(1),
            titulo = "titulo",
            resumo = "resumo",
            sumario = "suamrio",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isbn",
            categoriaId = this.categoria.id!!,
            autorId = this.autor.id!!
        )

        val responseGrpc = this.grpcClient.cadastrar(requestGrpc)

        with(responseGrpc) {
            assertNotNull(this.id)
        }
    }

    @Test
    internal fun `deve retornar INVALID_ARGUMENT`() {
        val requestGrpc = this.criarRequestGrpc(
            data = LocalDate.now().plusDays(1),
            titulo = "",
            resumo = "resumo",
            sumario = "suamrio",
            precoScale = 2,
            precoValor = 10050,
            paginas = 250,
            isbn = "isbn",
            categoriaId = this.categoria.id!!,
            autorId = this.autor.id!!
        )

        val error = assertThrows<StatusRuntimeException> { this.grpcClient.cadastrar(requestGrpc) }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, this.status.code)
        }
    }

    private fun criarRequestGrpc(
        data: LocalDate,
        titulo: String,
        resumo: String,
        sumario: String,
        precoScale: Int,
        precoValor: Long,
        paginas: Int,
        isbn: String,
        categoriaId: String,
        autorId: String
    ): CadastrarLivroRequestGrpc {
        val instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant()

        return CadastrarLivroRequestGrpc.newBuilder()
            .setTitulo(titulo)
            .setResumo(resumo)
            .setSumario(sumario)
            .setPreco(
                BigDecimal.newBuilder()
                    .setScale(precoScale)
                    .setValor(precoValor)
                    .build()
            )
            .setPaginas(paginas)
            .setIsbn(isbn)
            .setDataPublicacao(
                Timestamp.newBuilder()
                    .setSeconds(instant.epochSecond)
                    .setNanos(instant.nano)
                    .build()
            )
            .setCategoria(
                CadastrarLivroRequestGrpc.Categoria.newBuilder()
                    .setCategoriaId(categoriaId)
                    .build()
            )
            .setAutor(
                CadastrarLivroRequestGrpc.Autor.newBuilder()
                    .setAutorId(autorId)
                    .build()
            )
            .build()
    }

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

    @Factory
    class Class3 {

        @Singleton
        fun cadastrarLivro(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                CadastrarNovoLivroServiceGrpc.CadastrarNovoLivroServiceBlockingStub =
            CadastrarNovoLivroServiceGrpc.newBlockingStub(channel)
    }
}