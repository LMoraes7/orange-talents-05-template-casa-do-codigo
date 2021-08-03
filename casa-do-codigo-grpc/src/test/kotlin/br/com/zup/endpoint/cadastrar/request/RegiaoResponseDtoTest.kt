package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Estado
import br.com.zup.dominio.modelo.Pais
import br.com.zup.dominio.repository.EstadoRepository
import br.com.zup.dominio.repository.PaisRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.validation.Validator

@MicronautTest
internal class RegiaoResponseDtoTest(
    private val validator: Validator,
    private val paisRepository: PaisRepository,
    private val estadoRepository: EstadoRepository,
) {

    lateinit var brasil: Pais
    lateinit var eua: Pais
    lateinit var rj: Estado
    lateinit var nevada: Estado

    @BeforeEach
    internal fun setUp() {
        this.salvarPais("Brasil")
            .also { this.brasil = it }

        this.salvarPais("EUA")
            .also { this.eua = it }

        this.salvarEstado("Rio de Janeiro", this.brasil)
            .also { this.rj = it }

        this.salvarEstado("Nevada", this.eua)
            .also { this.nevada = it }
    }

    fun salvarPais(nome: String) =
        this.paisRepository.save(this.criarPais(nome))

    fun criarPais(nome: String) =
        Pais(nome = nome)

    fun salvarEstado(nome: String, pais: Pais) =
        this.estadoRepository.save(this.criarEstado(nome, pais))

    fun criarEstado(nome: String, pais: Pais) =
        Estado(
            nome = nome,
            pais = pais
        )

    @AfterEach
    fun close() {
        this.estadoRepository.deleteAll()
        this.paisRepository.deleteAll()
    }

    @Test
    internal fun `deve validar`() {
        val errors = this.criarRequestDto(this.brasil.id, this.rj.id)
            .run { validar(this) }
        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `paisId e estadoId nao devem ser nulos`() {
        var errors = this.criarRequestDto(null, this.rj.id)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto(this.brasil.id, null)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `paisId e estadoId devem existir`() {
        var errors = this.criarRequestDto(10, this.rj.id)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto(this.brasil.id, 10)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `deve falhar quando estado nao pertencer ao pais`() {
        val errors = this.criarRequestDto(this.brasil.id, this.nevada.id)
            .run { validar(this) }

        assertTrue(errors.isNotEmpty())
    }

    fun validar(dto: RegiaoResponseDto) =
        this.validator.validate(dto)

    fun criarRequestDto(paisId: Long?, estadoId: Long?) =
        RegiaoResponseDto(
            paisId = paisId,
            estadoId = estadoId
        )
}