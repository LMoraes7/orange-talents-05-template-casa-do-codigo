package br.com.zup.edu.endpoint.categoria.cadastrar

import br.com.zup.edu.CadastrarCategoriaRequestGrpc
import br.com.zup.edu.CadastrarNovaCategoriaServiceGrpc
import br.com.zup.edu.dominio.repository.CategoriaRepository
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
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class CadastrarCategoriaEndpointTest(
    private val grpcClient: CadastrarNovaCategoriaServiceGrpc.CadastrarNovaCategoriaServiceBlockingStub,
    private val repository: CategoriaRepository,
) {

    val request =
        CadastrarCategoriaRequestGrpc.newBuilder()
            .setNome("Esportes")

    @BeforeEach
    internal fun setUp() {
        this.repository.deleteAll()
    }

    @AfterEach
    fun close() {
        this.repository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar categoria`() {
        val response = this.grpcClient.cadastrar(this.request.build())

        with(response) {
            assertNotNull(response.id)
            assertEquals(1, repository.count())
        }
    }

    @Test
    internal fun `deve retornar INVALID_ARGUMENT`() {
        this.request.nome = ""

        val error = assertThrows<StatusRuntimeException> { this.grpcClient.cadastrar(this.request.build()) }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, this.status.code)
            assertEquals(0, repository.count())
        }
    }

    @Factory
    class Client2 {
        @Singleton
        fun cadastrarCategoria(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                CadastrarNovaCategoriaServiceGrpc.CadastrarNovaCategoriaServiceBlockingStub =
            CadastrarNovaCategoriaServiceGrpc.newBlockingStub(channel)
    }
}