package br.com.zup.dominio.modelo

import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.FetchType.*

@Entity
class Livro(
    @Column(nullable = false, unique = true)
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
    @Column(nullable = false)
    val dataPublicacao: LocalDate,
    @JoinColumn(nullable = false)
    @ManyToOne(
        cascade = [MERGE, REMOVE],
        optional = false,
        fetch = LAZY
    )
    val autor: Autor,
    @JoinColumn(nullable = false)
    @ManyToOne(
        cascade = [MERGE, REMOVE],
        optional = false,
        fetch = LAZY
    )
    val categoria: Categoria,
) {

    init {
        this.autor.addLivro(this)
        this.categoria.addLivro(this)
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    private set
}
