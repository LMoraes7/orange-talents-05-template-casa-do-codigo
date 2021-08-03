package br.com.zup.dominio.util

import br.com.zup.dominio.repository.AutorRepository
import br.com.zup.dominio.repository.CategoriaRepository
import br.com.zup.dominio.repository.EstadoRepository
import br.com.zup.dominio.repository.PaisRepository
import br.com.zup.endpoint.cadastrar.request.EstadoRequestDto

class BuscarEntidade {

    companion object {
        fun pais(
            paisRepository: PaisRepository,
            id: Long,
        ) = paisRepository.findById(id).get()

        fun estado(
            estadoRepository: EstadoRepository,
            id: Long
        ) = estadoRepository.findById(id).get()

        fun autor(
            autorRepository: AutorRepository,
            id: Long
        ) = autorRepository.findById(id).get()

        fun categoria(
            categoriaRepository: CategoriaRepository,
            id: Long
        ) = categoriaRepository.findById(id).get()
    }
}