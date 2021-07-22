package br.com.zup.edu.endpoint.categoria.cadastrar

import br.com.zup.edu.CadastrarCategoriaRequestGrpc
import br.com.zup.edu.CadastrarCategoriaResponseGrpc
import br.com.zup.edu.CadastrarNovaCategoriaServiceGrpc
import br.com.zup.edu.dominio.modelo.Categoria
import br.com.zup.edu.dominio.repository.CategoriaRepository
import br.com.zup.edu.endpoint.categoria.request.NovaCategoriaRequestDto
import br.com.zup.edu.endpoint.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarCategoriaEndpoint(
    private val validator: Validator,
    private val repository: CategoriaRepository,
) : CadastrarNovaCategoriaServiceGrpc.CadastrarNovaCategoriaServiceImplBase() {

    @Transactional
    override fun cadastrar(
        request: CadastrarCategoriaRequestGrpc,
        responseObserver: StreamObserver<CadastrarCategoriaResponseGrpc>,
    ) {
        val categoria = request.paraCategoriaModel(this.validator)

        this.repository.save(categoria)
            .run {
                CadastrarCategoriaResponseGrpc.newBuilder()
                    .setId(this.id)
                    .build()
            }.let {
                responseObserver.onNext(it)
            }

        responseObserver.onCompleted()
    }
}

private fun CadastrarCategoriaRequestGrpc.paraCategoriaModel(validator: Validator): Categoria {
    val dto = NovaCategoriaRequestDto(
        nome = this.nome
    )

    val errors = validator.validate(dto)

    if (errors.isNotEmpty())
        throw ConstraintViolationException(errors)

    return dto.paraCategoriaModel()
}
