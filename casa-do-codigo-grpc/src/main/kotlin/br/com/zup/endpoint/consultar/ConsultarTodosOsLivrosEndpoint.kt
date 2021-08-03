package br.com.zup.endpoint.consultar

import br.com.zup.ConsultarTodosLivrosRequestGrpc
import br.com.zup.ConsultarTodosLivrosResponseGrpc
import br.com.zup.ConsultarTodosLivrosServiceGrpc
import br.com.zup.dominio.modelo.Livro
import br.com.zup.dominio.repository.LivroRepository
import br.com.zup.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@ErrorAroundHandler
class ConsultarTodosOsLivrosEndpoint(
    private val repository: LivroRepository,
) : ConsultarTodosLivrosServiceGrpc.ConsultarTodosLivrosServiceImplBase() {

    @Transactional
    override fun consultar(
        request: ConsultarTodosLivrosRequestGrpc,
        responseObserver: StreamObserver<ConsultarTodosLivrosResponseGrpc>,
    ) {
        val livrosResponseGrpc = this.repository.findAll()
            .map { this.paraLivroResponseGrpc(it) }
            .run {
                ConsultarTodosLivrosResponseGrpc.newBuilder()
                    .addAllLivros(this)
                    .build()
            }

        responseObserver.onNext(livrosResponseGrpc)
        responseObserver.onCompleted()
    }

    private fun paraLivroResponseGrpc(livro: Livro) =
        ConsultarTodosLivrosResponseGrpc.Livros.newBuilder()
            .setId(livro.id!!)
            .setTitulo(livro.titulo)
            .build()
}