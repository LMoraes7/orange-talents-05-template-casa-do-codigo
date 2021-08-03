package br.com.zup.dominio.modelo

import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.FetchType.*

@Entity
class Estado(
    @Column(nullable = false, unique = true)
    val nome: String,
    @JoinColumn(nullable = false)
    @ManyToOne(
        fetch = LAZY,
        cascade = [MERGE, REMOVE],
        optional = false
    )
    val pais: Pais,
) {

    init {
        this.pais.addEstado(this)
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    private set
}