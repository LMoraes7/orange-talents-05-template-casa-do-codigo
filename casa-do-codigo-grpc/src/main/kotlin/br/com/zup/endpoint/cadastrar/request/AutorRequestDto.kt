package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Autor
import br.com.zup.dominio.validacao.ValorUnico
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
class AutorRequestDto(
    @field:NotBlank
    val nome: String?,
    @field:NotBlank
    @field:Email
    @field:ValorUnico(entidade = Autor::class, campo = "email")
    val email: String?,
    @field:NotBlank
    @field:Size(max = 400)
    val descricao: String?,
): RequestDto {
    fun paraModel() =
        Autor(
            nome = this.nome!!,
            email = this.email!!,
            descricao = this.descricao!!
        )

}
