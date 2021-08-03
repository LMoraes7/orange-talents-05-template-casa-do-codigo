package br.com.zup.dominio.modelo

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.FetchType.*

@Entity
class Autor(
    @Column(nullable = false)
    val nome: String,
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    val descricao: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    private set

    @Column(nullable = false)
    val registradoEm: LocalDateTime = LocalDateTime.now()

    @OneToMany(
        cascade = [ALL],
        fetch = LAZY,
        mappedBy = "autor"
    )
    private val livros: MutableList<Livro> = mutableListOf()

    fun addLivro(livro: Livro) {
        this.livros.add(livro)
    }

    fun getLivros(): List<Livro> =
        Collections.unmodifiableList(this.livros.toList())
}