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
internal class EstadoRequestDtoTest(
    private val validator: Validator,
    private val paisRepository: PaisRepository,
    private val estadoRepository: EstadoRepository,
) {

    lateinit var pais: Pais
    lateinit var estado: Estado

    @BeforeEach
    internal fun setUp() {
        this.salvarPais("Brasil")
            .also { this.pais = it }

        this.salvarEstado("São Paulo")
            .also { this.estado = it }
    }

    @AfterEach
    fun close() {
        this.estadoRepository.deleteAll()
        this.paisRepository.deleteAll()
    }

    @Test
    internal fun `deve validar`() {
        val errors = this.criarRequestDto(
            nome = "Rio de Janeiro",
            paisId = this.pais.id
        ).run {
            validar(this)
        }

        assertTrue(errors.isEmpty())
    }

    @Test
    internal fun `nome nao deve ser nulo e vazio`() {
        var errors = this.criarRequestDto(null, this.pais.id)
            .run {
                validar(this)
            }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto("", this.pais.id)
            .run {
                validar(this)
            }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `paisId nao deve ser nulo e invalido`() {
        var errors = this.criarRequestDto("Rio de Janeiro", null)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())

        errors = this.criarRequestDto("Rio de Janeiro", 10)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())
    }

    @Test
    internal fun `nome nao deve ser duplicado`() {
        val errors = this.criarRequestDto(this.estado.nome, this.pais.id)
            .run { validar(this) }
        assertTrue(errors.isNotEmpty())
    }

    fun validar(dto: EstadoRequestDto) =
        this.validator.validate(dto)

    fun salvarEstado(nome: String) =
        this.estadoRepository.save(this.criarEstado(nome, this.pais))

    fun criarEstado(nome: String, pais: Pais) =
        Estado(
            nome = nome,
            pais = pais
        )

    fun salvarPais(nome: String) =
        this.paisRepository.save(this.criarPais(nome = nome))

    fun criarRequestDto(nome: String?, paisId: Long?) =
        EstadoRequestDto(
            nome = nome,
            paisId = paisId
        )

    fun criarPais(nome: String) =
        Pais(nome = nome)
}