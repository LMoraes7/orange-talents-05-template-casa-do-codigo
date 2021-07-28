package br.com.zup.edu.dominio.modelo

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
data class Autor(
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    val nome: String,
    @Column(nullable = false)
    val descricao: String,
) {

    @Id
    var id: String? = (UUID.randomUUID().toString() + LocalDateTime.now().toString())

    @Column(nullable = false, columnDefinition = "DATETIME")
    val registradoEm = LocalDateTime.now()

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        mappedBy = "autor"
    )
    private var livros: MutableList<Livro> = mutableListOf()

    fun addLivro(livro: Livro) {
        this.livros.add(livro)
    }

    fun getLivro(): List<Livro> = this.livros.toList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Autor

        if (email != other.email) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}
