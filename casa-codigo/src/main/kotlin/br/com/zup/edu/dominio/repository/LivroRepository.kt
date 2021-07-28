package br.com.zup.edu.dominio.repository

import br.com.zup.edu.dominio.modelo.Livro
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface LivroRepository: JpaRepository<Livro, String> {

}
