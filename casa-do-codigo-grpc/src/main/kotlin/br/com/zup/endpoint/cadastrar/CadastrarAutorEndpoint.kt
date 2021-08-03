package br.com.zup.endpoint.cadastrar

import br.com.zup.CadastrarAutorServiceGrpc
import br.com.zup.NovoAutorRequestGrpc
import br.com.zup.NovoAutorResponseGrpc
import br.com.zup.dominio.modelo.Autor
import br.com.zup.dominio.repository.AutorRepository
import br.com.zup.dominio.util.RealizarValidacao
import br.com.zup.endpoint.cadastrar.request.AutorRequestDto
import br.com.zup.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarAutorEndpoint(
    val validator: Validator,
    val repository: AutorRepository,
) : CadastrarAutorServiceGrpc.CadastrarAutorServiceImplBase() {

    @Transactional
    override fun cadastrar(
        request: NovoAutorRequestGrpc,
        responseObserver: StreamObserver<NovoAutorResponseGrpc>,
    ) {
        val autorResponseGrpc = request.paraAutorModel(this.validator)
            .run {
                repository.save(this)
            }.let {
                NovoAutorResponseGrpc.newBuilder()
                    .setId(it.id!!)
                    .build()
            }

        responseObserver.onNext(autorResponseGrpc)
        responseObserver.onCompleted()
    }
}

private fun NovoAutorRequestGrpc.paraAutorModel(validator: Validator): Autor {
    val dto = AutorRequestDto(
        nome = this.nome,
        email = this.email,
        descricao = this.descricao
    )
    RealizarValidacao.validar(validator, dto)
    return dto.paraModel()
}