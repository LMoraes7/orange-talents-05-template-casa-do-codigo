package br.com.zup.endpoint.cadastrar

import br.com.zup.BDecimal
import br.com.zup.CadastrarLivroServiceGrpc
import br.com.zup.NovoLivroRequestGrpc
import br.com.zup.NovoLivroResponseGrpc
import br.com.zup.dominio.modelo.Livro
import br.com.zup.dominio.repository.AutorRepository
import br.com.zup.dominio.repository.CategoriaRepository
import br.com.zup.dominio.repository.LivroRepository
import br.com.zup.dominio.util.BuscarEntidade
import br.com.zup.dominio.util.RealizarValidacao
import br.com.zup.endpoint.cadastrar.request.LivroRequestDto
import br.com.zup.handler.ErrorAroundHandler
import com.google.protobuf.ByteString
import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Validator

@Singleton
@ErrorAroundHandler
class CadastrarLivroEndpoint(
    private val validator: Validator,
    private val autorRepository: AutorRepository,
    private val categoriaRepository: CategoriaRepository,
    private val livroRepository: LivroRepository,

    ) : CadastrarLivroServiceGrpc.CadastrarLivroServiceImplBase() {

    @Transactional
    override fun cadastrar(
        request: NovoLivroRequestGrpc,
        responseObserver: StreamObserver<NovoLivroResponseGrpc>,
    ) {
        val livroResponseGrpc = request.paraLivroModel(
            this.validator,
            this.autorRepository,
            this.categoriaRepository
        ).run {
            livroRepository.save(this)
        }.let {
            NovoLivroResponseGrpc.newBuilder()
                .setId(it.id!!)
                .build()
        }

        responseObserver.onNext(livroResponseGrpc)
        responseObserver.onCompleted()
    }
}

private fun NovoLivroRequestGrpc.paraLivroModel(
    validator: Validator,
    autorRepository: AutorRepository,
    categoriaRepository: CategoriaRepository,
): Livro {
    val dto = LivroRequestDto(
        titulo = this.titulo,
        resumo = this.resumo,
        sumario = this.sumario,
        preco = transformarEmPreco(this.preco),
        paginas = this.paginas,
        isbn = this.isbn,
        dataPublicacao = transformarEmDataPublicacao(this.dataPublicacao),
        categoriaId = this.categoriaId,
        autorId = this.autorId
    )

    RealizarValidacao.validar(validator, dto)

    return dto.paraAutorModel(
        autor = BuscarEntidade.autor(autorRepository, dto.autorId!!),
        categoria = BuscarEntidade.categoria(categoriaRepository, dto.categoriaId!!)
    )
}

fun transformarEmDataPublicacao(dataPublicacao: Timestamp): LocalDate =
    Instant.ofEpochSecond(dataPublicacao.seconds, dataPublicacao.nanos.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

fun transformarEmPreco(preco: BDecimal): BigDecimal =
    preco.valorInt.valorBytes
        .run {
            BigInteger(this.toByteArray())
        }.let {
            BigDecimal(it).setScale(preco.scale, RoundingMode.HALF_UP)
        }