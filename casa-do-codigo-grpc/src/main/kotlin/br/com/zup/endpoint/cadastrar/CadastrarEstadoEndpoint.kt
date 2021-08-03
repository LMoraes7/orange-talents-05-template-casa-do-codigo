package br.com.zup.endpoint.cadastrar

import br.com.zup.CadastrarEstadoServiceGrpc
import br.com.zup.NovoEstadoRequestGrpc
import br.com.zup.NovoEstadoResponseGrpc
import br.com.zup.dominio.modelo.Estado
import br.com.zup.dominio.repository.EstadoRepository
import br.com.zup.dominio.repository.PaisRepository
import br.com.zup.dominio.util.BuscarEntidade
import br.com.zup.dominio.util.RealizarValidacao
import br.com.zup.endpoint.cadastrar.request.EstadoRequestDto
import br.com.zup.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarEstadoEndpoint(
    private val validator: Validator,
    private val paisRepository: PaisRepository,
    private val estadoRepository: EstadoRepository,
) : CadastrarEstadoServiceGrpc.CadastrarEstadoServiceImplBase() {

    @Transactional
    override fun cadastrar(
        request: NovoEstadoRequestGrpc,
        responseObserver: StreamObserver<NovoEstadoResponseGrpc>,
    ) {
        val estadoResponseGrpc = request.paraEstadoModel(
            this.validator,
            this.paisRepository
        ).run { estadoRepository.save(this) }
            .let {
                NovoEstadoResponseGrpc.newBuilder()
                    .setId(it.id!!)
                    .build()
            }

        responseObserver.onNext(estadoResponseGrpc)
        responseObserver.onCompleted()
    }
}

private fun NovoEstadoRequestGrpc.paraEstadoModel(
    validator: Validator,
    paisRepository: PaisRepository,
): Estado {
    val dto = EstadoRequestDto(
        nome = this.nome,
        paisId = this.paisId
    )
    RealizarValidacao.validar(validator, dto)
    return dto.paraEstadoModel(BuscarEntidade.pais(paisRepository, dto.paisId!!))
}


