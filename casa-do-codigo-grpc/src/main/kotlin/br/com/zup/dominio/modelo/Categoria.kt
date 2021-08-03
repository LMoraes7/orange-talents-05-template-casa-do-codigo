package br.com.zup.dominio.modelo

import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.FetchType.*

@Entity
class Categoria(
    @Column(nullable = false, unique = true)
    val nome: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    private set

    @OneToMany(
        cascade = [ALL],
        fetch = LAZY,
        mappedBy = "categoria"
    )
    private val livros: MutableList<Livro> = mutableListOf()

    fun addLivro(livro: Livro) {
        this.livros.add(livro)
    }

    fun getLivros(): List<Livro> =
        Collections.unmodifiableList(this.livros.toList())
}