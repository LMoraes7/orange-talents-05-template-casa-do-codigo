package br.com.zup.endpoint.consultar

import br.com.zup.BDecimal
import br.com.zup.ConsultarLivroRequestGrpc
import br.com.zup.ConsultarLivroResponseGrpc
import br.com.zup.ConsultarLivroServiceGrpc
import br.com.zup.dominio.exception.LivroInexistenteException
import br.com.zup.dominio.modelo.Livro
import br.com.zup.dominio.repository.LivroRepository
import br.com.zup.handler.ErrorAroundHandler
import com.google.protobuf.ByteString
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@ErrorAroundHandler
class ConsultarLivroEndpoint(
    private val repository: LivroRepository,
) : ConsultarLivroServiceGrpc.ConsultarLivroServiceImplBase() {

    @Transactional
    override fun consultar(
        request: ConsultarLivroRequestGrpc,
        responseObserver: StreamObserver<ConsultarLivroResponseGrpc>,
    ) {
        val livroResponseGrpc = this.repository.consultarComAutor(request.id)
            .map { paraLivroResponseGrpc(it) }
            .orElseThrow { LivroInexistenteException("O livro informado não existe") }

        responseObserver.onNext(livroResponseGrpc)
        responseObserver.onCompleted()
    }

    private fun paraLivroResponseGrpc(livro: Livro) =
        ConsultarLivroResponseGrpc.newBuilder()
            .setTitulo(livro.titulo)
            .setResumo(livro.resumo)
            .setSumario(livro.sumario)
            .setPreco(
                BDecimal.newBuilder()
                    .setScale(livro.preco.scale())
                    .setValorInt(
                        BDecimal.BInteger.newBuilder()
                            .setValorBytes(ByteString.copyFrom(livro.preco.toBigInteger().toByteArray()))
                            .build()
                    )
                    .build()
            )
            .setPaginas(livro.paginas)
            .setIsbn(livro.isbn)
            .setAutor(
                ConsultarLivroResponseGrpc.DetalhesAutor.newBuilder()
                    .setNome(livro.autor.nome)
                    .setDescricao(livro.autor.descricao)
                    .build()
            )
            .build()
}