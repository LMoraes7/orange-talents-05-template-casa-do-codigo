package br.com.zup.endpoint.cadastrar

import br.com.zup.CadastrarClienteServiceGrpc
import br.com.zup.NovoClienteRequestGrpc
import br.com.zup.NovoClienteResponseGrpc
import br.com.zup.dominio.modelo.Cliente
import br.com.zup.dominio.repository.ClienteRepository
import br.com.zup.dominio.repository.EstadoRepository
import br.com.zup.dominio.repository.PaisRepository
import br.com.zup.dominio.util.BuscarEntidade
import br.com.zup.dominio.util.RealizarValidacao
import br.com.zup.endpoint.cadastrar.request.ClienteRequestDto
import br.com.zup.endpoint.cadastrar.request.EnderecoResponseDto
import br.com.zup.endpoint.cadastrar.request.RegiaoResponseDto
import br.com.zup.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarClientEndpoint(
    private val validator: Validator,
    private val paisRepository: PaisRepository,
    private val estadoRepository: EstadoRepository,
    private val clienteRepository: ClienteRepository,
) : CadastrarClienteServiceGrpc.CadastrarClienteServiceImplBase() {

    override fun cadastrar(
        request: NovoClienteRequestGrpc,
        responseObserver: StreamObserver<NovoClienteResponseGrpc>,
    ) {
        val clienteResponseGrpc = request.paraClienteModel(
            this.validator,
            this.paisRepository,
            this.estadoRepository
        ).run { clienteRepository.save(this) }
            .let {
                NovoClienteResponseGrpc.newBuilder()
                    .setId(it.id!!)
                    .build()
            }

        responseObserver.onNext(clienteResponseGrpc)
        responseObserver.onCompleted()
    }
}

private fun NovoClienteRequestGrpc.paraClienteModel(
    validator: Validator,
    paisRepository: PaisRepository,
    estadoRepository: EstadoRepository,
): Cliente {
    val dto = ClienteRequestDto(
        email = this.email,
        nome = this.nome,
        sobrenome = this.sobrenome,
        documento = this.documento,
        endereco = EnderecoResponseDto(
            logradouro = this.endereco.logradouro,
            complemento = this.endereco.complemento,
            cidade = this.endereco.cidade,
            cep = this.endereco.cep
        ),
        regiao = RegiaoResponseDto(
            paisId = this.regiao.paisId,
            estadoId = this.regiao.estadoId
        ),
        telefone = this.telefone
    )
    RealizarValidacao.validar(validator, dto)
    return dto.paraClienteModel(
        pais = BuscarEntidade.pais(paisRepository, dto.regiao.paisId!!),
        estado = BuscarEntidade.estado(estadoRepository, dto.regiao.estadoId!!)
    )
}

