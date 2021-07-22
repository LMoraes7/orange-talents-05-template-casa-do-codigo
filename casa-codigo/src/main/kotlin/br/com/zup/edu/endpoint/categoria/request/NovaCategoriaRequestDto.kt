package br.com.zup.edu.endpoint.categoria.request

import br.com.zup.edu.dominio.modelo.Categoria
import br.com.zup.edu.dominio.validacao.ValorUnico
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class NovaCategoriaRequestDto(
    @field:NotBlank(message = "nome deve ser informado")
    @field:ValorUnico(entidade = Categoria::class, campo = "nome", message = "categoria informada já foi cadastrada")
    val nome: String?,
) {
    fun paraCategoriaModel() =
        Categoria(nome = this.nome!!)

}
