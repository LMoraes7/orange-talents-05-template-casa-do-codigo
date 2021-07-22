package br.com.zup.edu.endpoint.autor.cadastrar

import br.com.zup.edu.CadastrarAutorRequestGrpc
import br.com.zup.edu.CadastrarAutorResponseGrpc
import br.com.zup.edu.CadastrarNovoAutorServiceGrpc
import br.com.zup.edu.dominio.modelo.Autor
import br.com.zup.edu.dominio.repository.AutorRespository
import br.com.zup.edu.endpoint.autor.request.NovoAutorRequestDto
import br.com.zup.edu.endpoint.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarAutorEnpoint(
    private val validator: Validator,
    private val repository: AutorRespository,
) : CadastrarNovoAutorServiceGrpc.CadastrarNovoAutorServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun cadastrar(
        request: CadastrarAutorRequestGrpc,
        responseObserver: StreamObserver<CadastrarAutorResponseGrpc>,
    ) {
        this.logger.info("endpoint -> recebendo requisição para cadastro de novo autor")
        this.logger.info("endpoint -> efetuando as validações de entrada para a requisição")

        val autor = request.paraAutorModel(this.validator)

        this.logger.info("endpoint -> salvando novo autor na base de dados")

        this.repository.save(autor)
            .run {
                CadastrarAutorResponseGrpc.newBuilder()
                    .setId(this.id)
                    .build()
            }.let {
                responseObserver.onNext(it)
            }

        this.logger.info("endpoint -> retornando resposta para o usuário")

        responseObserver.onCompleted()
    }
}

private fun CadastrarAutorRequestGrpc.paraAutorModel(validator: Validator): Autor {
    val dto = NovoAutorRequestDto(
        email = this.email,
        nome = this.nome,
        descricao = this.descricao
    )

    val errors = validator.validate(dto)

    if (errors.isNotEmpty())
        throw ConstraintViolationException(errors)

    return dto.paraAutorModel()
}
