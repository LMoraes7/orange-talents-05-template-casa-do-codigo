package br.com.zup.endpoint.cadastrar

import br.com.zup.CadastrarAutorServiceGrpc
import br.com.zup.NovoAutorRequestGrpc
import br.com.zup.dominio.repository.AutorRepository
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
internal class CadastrarAutorEndpointTest(
    private val grpcClient: CadastrarAutorServiceGrpc.CadastrarAutorServiceBlockingStub,
    private val repository: AutorRepository,
) {

    @AfterEach
    fun close() {
        this.repository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar`() {
        val responseGrpc = this.criarRequestGrpc(
            "nome",
            "email@email.com",
            "descrição"
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
            "",
            "",
            ""
        )

        val error = assertThrows<StatusRuntimeException> { this.grpcClient.cadastrar(requestGrpc) }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, this.status.code)
        }
    }

    fun criarRequestGrpc(
        nome: String,
        email: String,
        descricao: String,
    ) =
        NovoAutorRequestGrpc.newBuilder()
            .setNome(nome)
            .setEmail(email)
            .setDescricao(descricao)
            .build()

    @Factory
    class CadastrarAutor {

        @Singleton
        fun cadastrar(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                CadastrarAutorServiceGrpc.CadastrarAutorServiceBlockingStub =
            CadastrarAutorServiceGrpc.newBlockingStub(channel)
    }
}