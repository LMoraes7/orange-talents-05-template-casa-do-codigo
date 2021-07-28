package br.com.zup.edu.dominio.modelo

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Categoria(
    @Column(nullable = false, unique = true)
    val nome: String,
) {

    @Id
    var id: String? = (UUID.randomUUID().toString() + LocalDateTime.now().toString())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Categoria

        if (nome != other.nome) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nome.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}