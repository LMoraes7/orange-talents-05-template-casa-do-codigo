package br.com.zup.endpoint.cadastrar

import br.com.zup.CadastrarCategoriaServiceGrpc
import br.com.zup.NovaCategoriaRequestGrpc
import br.com.zup.NovaCategoriaResponseGrpc
import br.com.zup.dominio.modelo.Categoria
import br.com.zup.dominio.repository.CategoriaRepository
import br.com.zup.dominio.util.RealizarValidacao
import br.com.zup.endpoint.cadastrar.request.CategoriaRequestDto
import br.com.zup.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarCategoriaEnpoint(
    private val validator: Validator,
    private val repository: CategoriaRepository,
) : CadastrarCategoriaServiceGrpc.CadastrarCategoriaServiceImplBase() {

    @Transactional
    override fun cadastrar(
        request: NovaCategoriaRequestGrpc,
        responseObserver: StreamObserver<NovaCategoriaResponseGrpc>,
    ) {
        val categoriaResponseGrpc =
            request.paraCategoriaModel(this.validator)
                .run {
                    repository.save(this)
                }.let {
                    NovaCategoriaResponseGrpc.newBuilder()
                        .setId(it.id!!)
                        .build()
                }

        responseObserver.onNext(categoriaResponseGrpc)
        responseObserver.onCompleted()
    }
}

private fun NovaCategoriaRequestGrpc.paraCategoriaModel(validator: Validator): Categoria {
    val dto = CategoriaRequestDto(
        nome = this.nome
    )
    RealizarValidacao.validar(validator, dto)
    return dto.paraCategoria()
}
