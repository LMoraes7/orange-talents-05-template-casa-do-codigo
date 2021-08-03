package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Autor
import br.com.zup.dominio.modelo.Categoria
import br.com.zup.dominio.modelo.Livro
import br.com.zup.dominio.validacao.ExisteEntidade
import br.com.zup.dominio.validacao.ValorUnico
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.*

@Introspected
class LivroRequestDto(
    @field:NotBlank
    @field:ValorUnico(entidade = Livro::class, campo = "titulo")
    val titulo: String?,
    @field:NotBlank
    @field:Size(max = 500)
    val resumo: String?,
    @field:NotBlank
    val sumario: String?,
    @field:NotNull
    @field:DecimalMin(value = "20.00")
    val preco: BigDecimal?,
    @field:NotNull
    @field:Min(value = 100)
    val paginas: Int?,
    @field:NotBlank
    val isbn: String?,
    @field:NotNull
    @field:Future
    val dataPublicacao: LocalDate?,
    @field:NotNull
    @field:ExisteEntidade(entidade = Categoria::class, campo = "id")
    val categoriaId: Long?,
    @field:NotNull
    @field:ExisteEntidade(entidade = Autor::class, campo = "id")
    val autorId: Long?,
): RequestDto {

    fun paraAutorModel(autor: Autor, categoria: Categoria) =
        Livro(
            titulo = this.titulo!!,
            resumo = this.resumo!!,
            sumario = this.sumario!!,
            preco = this.preco!!,
            paginas = this.paginas!!,
            isbn = this.isbn!!,
            dataPublicacao = this.dataPublicacao!!,
            autor = autor,
            categoria = categoria
        )
}
