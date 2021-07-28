package br.com.zup.edu.endpoint.livro

import br.com.zup.edu.CadastrarLivroRequestGrpc
import br.com.zup.edu.CadastrarLivroResponseGrpc
import br.com.zup.edu.CadastrarNovoLivroServiceGrpc
import br.com.zup.edu.dominio.exception.ValidacaoException
import br.com.zup.edu.dominio.modelo.Livro
import br.com.zup.edu.dominio.repository.AutorRespository
import br.com.zup.edu.dominio.repository.CategoriaRepository
import br.com.zup.edu.dominio.repository.LivroRepository
import br.com.zup.edu.endpoint.livro.request.NovoLivroRequestDto
import br.com.zup.edu.handler.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarLivroEnpoint(
    private val validator: Validator,
    private val autorRepository: AutorRespository,
    private val categoriaRepository: CategoriaRepository,
    private val livroRepository: LivroRepository,
) : CadastrarNovoLivroServiceGrpc.CadastrarNovoLivroServiceImplBase() {

    @Transactional
    override fun cadastrar(
        request: CadastrarLivroRequestGrpc,
        responseObserver: StreamObserver<CadastrarLivroResponseGrpc>,
    ) {
        val responseGrpc = request.paraLivroModel(
            this.validator,
            this.autorRepository,
            this.categoriaRepository
        ).run {
            livroRepository.save(this)
        }.let {
            CadastrarLivroResponseGrpc.newBuilder()
                .setId(it.id)
                .build()
        }

        responseObserver.onNext(responseGrpc)
        responseObserver.onCompleted()
    }
}

private fun CadastrarLivroRequestGrpc.paraLivroModel(
    validator: Validator,
    autorRepository: AutorRespository,
    categoriaRepository: CategoriaRepository,
): Livro {
    val dto = NovoLivroRequestDto(
        titulo = this.titulo,
        resumo = this.resumo,
        sumario = this.sumario,
        precoScale = this.preco?.scale,
        precoValor = this.preco?.valor,
        paginas = this.paginas,
        isbn = this.isbn,
        dataPublicacao = criarDataPublicacao(),
        categoriaId = this.categoria?.categoriaId,
        autorId = this.autor?.autorId
    )

    executarValidacao(validator, dto)

    val autor = autorRepository.findById(dto.autorId!!).get()
    val categoria = categoriaRepository.findById(dto.categoriaId!!).get()

    return dto.paraLivroModel(categoria, autor)
}

private fun executarValidacao(
    validator: Validator,
    dto: NovoLivroRequestDto,
) {
    val errors = validator.validate(dto)

    if (errors.isNotEmpty())
        throw ConstraintViolationException(errors)
}

private fun CadastrarLivroRequestGrpc.criarDataPublicacao(): LocalDate {
    if (this.dataPublicacao == null)
        throw ValidacaoException("Valor do TimeStamp deve ser informado")

    return Instant
        .ofEpochSecond(this.dataPublicacao.seconds, this.dataPublicacao.nanos.toLong())
        .atZone(ZoneId.of("America/Sao_Paulo"))
        .toLocalDate()
}