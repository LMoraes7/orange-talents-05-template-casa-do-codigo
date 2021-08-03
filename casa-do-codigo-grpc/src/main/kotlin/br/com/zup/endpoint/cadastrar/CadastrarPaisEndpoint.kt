package br.com.zup.endpoint.cadastrar

import br.com.zup.CadastrarPaisServiceGrpc
import br.com.zup.NovoPaisRequestGrpc
import br.com.zup.NovoPaisResponseGrpc
import br.com.zup.dominio.modelo.Pais
import br.com.zup.dominio.repository.PaisRepository
import br.com.zup.dominio.util.RealizarValidacao
import br.com.zup.endpoint.cadastrar.request.PaisRequestDto
import br.com.zup.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarPaisEndpoint(
    private val validator: Validator,
    private val repository: PaisRepository
): CadastrarPaisServiceGrpc.CadastrarPaisServiceImplBase() {

    @Transactional
    override fun cadastrar(
        request: NovoPaisRequestGrpc,
        responseObserver: StreamObserver<NovoPaisResponseGrpc>,
    ) {
        val paisResponseGrpc = request.paraPaisModel(this.validator)
            .run {
                repository.save(this)
            }.let {
                NovoPaisResponseGrpc.newBuilder()
                    .setId(it.id!!)
                    .build()
            }

        responseObserver.onNext(paisResponseGrpc)
        responseObserver.onCompleted()
    }
}

private fun NovoPaisRequestGrpc.paraPaisModel(validator: Validator): Pais {
    val dto = PaisRequestDto(
        nome = this.nome
    )
    RealizarValidacao.validar(validator, dto)
    return dto.paraPaisModel()
}
