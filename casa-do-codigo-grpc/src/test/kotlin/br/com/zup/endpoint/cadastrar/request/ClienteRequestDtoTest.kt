package br.com.zup.endpoint.cadastrar.request

import br.com.zup.dominio.modelo.Cliente
import br.com.zup.dominio.modelo.Endereco
import br.com.zup.dominio.modelo.Estado
import br.com.zup.dominio.modelo.Pais
import br.com.zup.dominio.repository.ClienteRepository
import br.com.zup.dominio.repository.EstadoRepository
import br.com.zup.dominio.repository.PaisRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.validation.Validator

@MicronautTest
internal class ClienteRequestDtoTest(
    private val validator: Validator,
    private val paisRepository: PaisRepository,
    private val estadoRepository: EstadoRepository,
    private val clienteRepository: ClienteRepository,
) {

    lateinit var pais: Pais
    lateinit var estado: Estado
    lateinit var cliente: Cliente

    @BeforeEach
    internal fun setUp() {
        this.salvarPais("Brasil")
            .also { this.pais = it }

        this.salvarEstado("Rio de Janeiro")
            .also { this.estado = it }

        this.salvarCliente(
            email = "email@email.com",
            nome = "nome",
            sobrenome = "sobrenome",
            documento = "33678366724",
            logradouro = "logradouro",
            complemento = "complemento",
            cidade = "cidade",
            cep = "20725080",
            telefone = "999999999"
        ).also { this.cliente = it }
    }

    fun salvarPais(nome: String) =
        this.paisRepository.save(this.criarPais(nome))

    fun criarPais(nome: String) =
        Pais(nome = nome)

    fun salvarEstado(nome: String) =
        this.estadoRepository.save(this.criarEstado(nome, this.pais))

    fun criarEstado(nome: String, pais: Pais) =
        Estado(
            nome = nome,
            pais = pais
        )

    fun salvarCliente(
        email: String,
        nome: String,
        sobrenome: String,
        documento: String,
        logradouro: String,
        complemento: String?,
        cidade: String,
        cep: String,
        telefone: String,
    ) =
        this.clienteRepository.save(
            this.criarCliente(
                email = email,
                nome = nome,
                sobrenome = sobrenome,
                documento = documento,
                logradouro = logradouro,
                complemento = complemento,
                cidade = cidade,
                cep = cep,
                telefone = telefone
            )
        )

    fun criarCliente(
        email: String,
        nome: String,
        sobrenome: String,
        documento: String,
        logradouro: String,
        complemento: String?,
        cidade: String,
        cep: String,
        telefone: String,
    ) =
        Cliente(
            email = email,
            nome = nome,
            sobrenome = sobrenome,
            documento = documento,
            endereco = Endereco(
                logradouro = logradouro,
                complemento = complemento,
                cidade = cidade,
                cep = cep
            ),
            telefone = telefone,
            pais = pais,
            estado = estado
        )

    @Test
    internal fun `deve validar`() {
        val errors = this.criarRequestDto(
            email = "email@emailll.com",
            nome = "nome",
            sobrenome = "sobrenome",
            documento = "38766698120",
            logradouro = "logradouro",
            complemento = "complemento",
            cidade = "cidade",
            cep = "20725080",
            paisId = this.pais.id,
            estadoId = this.estado.id,
            telefone = "+5521999999999"
        ).run {
            validar(this)
        }
        assertTrue(errors.isEmpty())
    }

    private fun criarRequestDto(
        email: String?,
        nome: String?,
        sobrenome: String?,
        documento: String?,
        logradouro: String?,
        complemento: String?,
        cidade: String?,
        cep: String?,
        paisId: Long?,
        estadoId: Long?,
        telefone: String?,
    ) = ClienteRequestDto(
        email = email,
        nome = nome,
        sobrenome = sobrenome,
        documento = documento,
        endereco = EnderecoResponseDto(
            logradouro = logradouro,
            complemento = complemento,
            cidade = cidade,
            cep = cep
        ),
        regiao = RegiaoResponseDto(
            paisId = paisId,
            estadoId = estadoId
        ),
        telefone = telefone
    )

    fun validar(dto: ClienteRequestDto) =
        this.validator.validate(dto)
}