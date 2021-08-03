package br.com.zup.dominio.repository

import br.com.zup.dominio.modelo.Estado
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface EstadoRepository: JpaRepository<Estado, Long> {

}
