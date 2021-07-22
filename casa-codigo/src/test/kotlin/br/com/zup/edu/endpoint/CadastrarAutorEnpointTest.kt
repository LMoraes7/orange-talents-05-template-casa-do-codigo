package br.com.zup.edu.endpoint

import br.com.zup.edu.CadastrarAutorRequestGrpc
import br.com.zup.edu.CadastrarNovoAutorServiceGrpc
import br.com.zup.edu.dominio.repository.AutorRespository
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
internal class CadastrarAutorEnpointTest(
    private val grpcClient: CadastrarNovoAutorServiceGrpc.CadastrarNovoAutorServiceBlockingStub,
    private val repository: AutorRespository
) {

    val request = CadastrarAutorRequestGrpc.newBuilder()
        .setEmail("diego@email.com")
        .setNome("Diego")
        .setDescricao("Descrição")

    @BeforeEach
    internal fun setUp() {
        this.repository.deleteAll()
    }

    @AfterEach
    internal fun close() {
        this.repository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar autor`() {
        val response = this.grpcClient.cadastrar(this.request.build())

        with(response) {
            assertNotNull(response.id)
            assertEquals(1, repository.count())
        }
    }

    @Test
    internal fun `deve retornar um erro INVALID_ARGUMENT`() {
        this.request.email = ""

        val error = assertThrows<StatusRuntimeException> { this.grpcClient.cadastrar(this.request.build()) }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, error.status.code)
            assertEquals(0, repository.count())
        }
    }

    @Factory
    class Client1 {

        @Singleton
        fun cadastrarAutor(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                CadastrarNovoAutorServiceGrpc.CadastrarNovoAutorServiceBlockingStub =
            CadastrarNovoAutorServiceGrpc.newBlockingStub(channel)
    }
}