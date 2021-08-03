package br.com.zup.dominio.repository

import br.com.zup.dominio.modelo.Livro
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface LivroRepository: JpaRepository<Livro, Long> {

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor WHERE l.id = :id")
    fun consultarComAutor(id: Long): Optional<Livro>
}
