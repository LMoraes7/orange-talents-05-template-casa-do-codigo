package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Pais
import br.com.zup.dominio.validacao.ValorUnico
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class PaisRequestDto(
    @field:NotBlank
    @field:ValorUnico(entidade = Pais::class, campo = "nome")
    val nome: String?,
): RequestDto {

    fun paraPaisModel() =
        Pais(nome = this.nome!!)

}
