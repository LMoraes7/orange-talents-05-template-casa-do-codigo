package br.com.zup.edu.dominio.modelo

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Livro(
    @Column(nullable = false)
    val titulo: String,
    @Column(nullable = false)
    val resumo: String,
    @Column(nullable = false)
    val sumario: String,
    @Column(nullable = false)
    val preco: BigDecimal,
    @Column(nullable = false)
    val paginas: Int,
    @Column(nullable = false)
    val isbn: String,
    @Column(nullable = false, columnDefinition = "DATETIME")
    val dataPublicacao: LocalDate,
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    val categoria: Categoria,
    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    val autor: Autor
) {

    init {
        this.autor.addLivro(this)
    }

    @Id
    var id: String? = (UUID.randomUUID().toString() + LocalDateTime.now().toString())

    @Column(nullable = false, columnDefinition = "DATE")
    val registradoEm: LocalDate = LocalDate.now()

//    @PrePersist
//    fun addLivroAoAutor() {
//        this.autor.addLivro(this)
//    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Livro

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}