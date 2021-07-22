package br.com.zup.edu.endpoint.dto.request

import br.com.zup.edu.dominio.modelo.Autor
import br.com.zup.edu.dominio.validacao.ValorUnico
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
class NovoAutorRequestDto(
    @field:NotBlank(message = "email deve ser informado")
    @field:Email(message = "email deve possuir formato válido")
    @field:ValorUnico(entidade = Autor::class, campo = "email", message = "email informado já foi cadastrado")
    val email: String?,
    @field:NotBlank(message = "nome deve ser informado")
    val nome: String?,
    @field:NotBlank(message = "descrição deve ser informada")
    @field:Size(min = 1, max = 400, message = "descrição informada deve possuir entre 1 e 400 caracteres")
    val descricao: String?,
) {
    fun paraAutorModel() =
        Autor(
            email = this.email!!,
            nome = this.nome!!,
            descricao = this.descricao!!
        )

}
