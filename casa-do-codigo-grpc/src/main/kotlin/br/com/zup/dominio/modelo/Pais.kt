package br.com.zup.dominio.modelo

import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.FetchType.*

@Entity
class Pais(
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
        mappedBy = "pais"
    )
    private val estados: MutableList<Estado> = mutableListOf()
    
    fun getEstados(): List<Estado> =
        Collections.unmodifiableList(this.estados.toList())

    fun addEstado(estado: Estado) {
        this.estados.add(estado)
    }
}
