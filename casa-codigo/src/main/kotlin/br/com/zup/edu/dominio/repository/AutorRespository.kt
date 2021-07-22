package br.com.zup.edu.dominio.repository

import br.com.zup.edu.dominio.modelo.Autor
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface AutorRespository: JpaRepository<Autor, String> {

}
