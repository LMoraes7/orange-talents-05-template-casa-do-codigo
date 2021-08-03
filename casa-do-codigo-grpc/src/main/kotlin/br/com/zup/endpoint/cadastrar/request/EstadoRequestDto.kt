package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Estado
import br.com.zup.dominio.modelo.Pais
import br.com.zup.dominio.validacao.ValidarNomeEstado
import io.micronaut.core.annotation.Introspected

@Introspected
@ValidarNomeEstado
class EstadoRequestDto(
    val nome: String?,
    val paisId: Long?,
): RequestDto {

    fun paraEstadoModel(pais: Pais) =
        Estado(
            nome = this.nome!!,
            pais = pais
        )
}
