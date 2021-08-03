package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Categoria
import br.com.zup.dominio.validacao.ValorUnico
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class CategoriaRequestDto(
    @field:NotBlank
    @field:ValorUnico(entidade = Categoria::class, campo = "nome")
    val nome: String?,
): RequestDto {

    fun paraCategoria() =
        Categoria(
            nome = this.nome!!
        )
}
