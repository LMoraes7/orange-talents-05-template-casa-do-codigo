package br.com.zup.dominio.modelo

import javax.persistence.*
import javax.persistence.FetchType.*

@Entity
class Cliente(
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    val nome: String,
    @Column(nullable = false)
    val sobrenome: String,
    @Column(nullable = false, unique = true)
    val documento: String,
    @Embedded
    val endereco: Endereco,
    @Column(nullable = false)
    val telefone: String,
    @JoinColumn(nullable = false)
    @ManyToOne(
        fetch = LAZY,
        optional = false
    )
    val pais: Pais,
    @JoinColumn(nullable = false)
    @ManyToOne(
        fetch = LAZY,
        optional = false
    )
    val estado: Estado
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    private set
}

@Embeddable
class Endereco(
    @Column(nullable = false)
    val logradouro: String,
    val complemento: String?,
    @Column(nullable = false)
    val cidade: String,
    @Column(nullable = false)
    val cep: String,
)
