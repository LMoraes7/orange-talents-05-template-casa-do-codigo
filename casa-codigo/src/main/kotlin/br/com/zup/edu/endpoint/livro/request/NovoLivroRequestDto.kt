package br.com.zup.edu.endpoint.livro.request

import br.com.zup.edu.dominio.modelo.Autor
import br.com.zup.edu.dominio.modelo.Categoria
import br.com.zup.edu.dominio.modelo.Livro
import br.com.zup.edu.dominio.validacao.ExisteEntidade
import com.google.protobuf.Timestamp
import io.micronaut.core.annotation.Introspected
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
class NovoLivroRequestDto(
    @field:NotBlank
    val titulo: String?,
    @field:NotBlank
    val resumo: String?,
    @field:NotBlank
    val sumario: String?,
    @field:NotNull
    val precoScale: Int?,
    @field:NotNull
    val precoValor: Long?,
    @field:NotNull
    val paginas: Int?,
    @field:NotBlank
    val isbn: String?,
    @field:NotNull
    @field:Future
    val dataPublicacao: LocalDate,
    @field:NotBlank
    @field:ExisteEntidade(entidade = Categoria::class, campo = "id")
    val categoriaId: String?,
    @field:NotBlank
    @field:ExisteEntidade(entidade = Autor::class, campo = "id")
    val autorId: String?,
) {

    fun paraLivroModel(categoria: Categoria, autor: Autor) =
        Livro(
            titulo = this.titulo!!,
            resumo = this.resumo!!,
            sumario = this.sumario!!,
            preco = this.createBigDecimal(),
            paginas = this.paginas!!,
            isbn = this.isbn!!,
            dataPublicacao = this.dataPublicacao!!,
            categoria = categoria,
            autor = autor
        )

    private fun createBigDecimal() =
        java.math.BigDecimal(this.precoValor!!).setScale(this.precoScale!!, RoundingMode.HALF_UP)
}