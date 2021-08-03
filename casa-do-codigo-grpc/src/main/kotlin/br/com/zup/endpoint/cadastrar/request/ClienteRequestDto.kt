package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Cliente
import br.com.zup.dominio.modelo.Endereco
import br.com.zup.dominio.modelo.Estado
import br.com.zup.dominio.modelo.Pais
import br.com.zup.dominio.validacao.*
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.Validated
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Introspected
@Validated
class ClienteRequestDto(
    @field:NotBlank
    @field:Email
    @field:ValorUnico(entidade = Cliente::class, campo = "email")
    val email: String?,
    @field:NotBlank
    val nome: String?,
    @field:NotBlank
    val sobrenome: String?,
    @field:NotBlank
//    @field:CPForCNPJ
    @field:ValidarDocumento
    @field:ValorUnico(entidade = Cliente::class, campo = "documento")
    val documento: String?,
    @field:Valid
    val endereco: EnderecoResponseDto,
    @field:Valid
    val regiao: RegiaoResponseDto,
    @field:Telefone
    val telefone: String?,
) : RequestDto {

    fun paraClienteModel(pais: Pais, estado: Estado) =
        Cliente(
            email = this.email!!,
            nome = this.nome!!,
            sobrenome = this.sobrenome!!,
            documento = this.documento!!,
            endereco = this.endereco.paraEnderecoModel(),
            telefone = this.telefone!!,
            pais = pais,
            estado = estado
        )
}

@Introspected
class EnderecoResponseDto(
    @field:NotBlank
    val logradouro: String?,
    val complemento: String?,
    @field:NotBlank
    val cidade: String?,
    @field:NotBlank
    @field:CEP
    val cep: String?,
) : RequestDto {
    fun paraEnderecoModel() =
        Endereco(
            logradouro = this.logradouro!!,
            complemento = this.complemento,
            cidade = this.cidade!!,
            cep = this.cep!!
        )
}

@Introspected
@ValidarPaisEhEstado
class RegiaoResponseDto(
    @field:ExisteEntidade(entidade = Pais::class, campo = "id")
    val paisId: Long?,
    @field:ExisteEntidade(entidade = Estado::class, campo = "id")
    val estadoId: Long?,
) : RequestDto
