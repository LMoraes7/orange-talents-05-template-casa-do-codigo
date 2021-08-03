package br.com.zup.endpoint.cadastrar

import br.com.zup.CadastrarPaisServiceGrpc
import br.com.zup.NovoPaisRequestGrpc
import br.com.zup.dominio.repository.PaisRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class CadastrarPaisEndpointTest(
    private val grpcClient: CadastrarPaisServiceGrpc.CadastrarPaisServiceBlockingStub,
    private val repository: PaisRepository,
) {

    @AfterEach
    fun close() {
        this.repository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar`() {
        val responseGrpc = this.criarRequestGrpc("Brasil")
            .run { grpcClient.cadastrar(this) }

        with(responseGrpc) {
            assertNotNull(this.id)
        }
    }

    @Test
    internal fun `deve retornar INVALID_ARGUMENT`() {
        val requestGrpc = this.criarRequestGrpc("")
        val error = assertThrows<StatusRuntimeException> { this.grpcClient.cadastrar(requestGrpc) }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, this.status.code)
        }
    }

    fun criarRequestGrpc(nome: String) =
        NovoPaisRequestGrpc.newBuilder()
            .setNome(nome)
            .build()

    @Factory
    class CadastrarPais {

        @Singleton
        fun cadastrar(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                CadastrarPaisServiceGrpc.CadastrarPaisServiceBlockingStub =
            CadastrarPaisServiceGrpc.newBlockingStub(channel)
    }
}