package br.com.zup.academy.dominio.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String titulo;
	@Column(nullable = false, columnDefinition = "VARCHAR(500)")
	private String resumo;
	@Column(columnDefinition = "TEXT")
	private String sumario;
	@Column(columnDefinition = "DECIMAL(5, 2) CHECK PRECO >= 20")
	private BigDecimal preco;
	@Column(columnDefinition = "SMALLINT CHECK PAGINAS >= 100")
	private Integer paginas;
	@Column(nullable = false, unique = true)
	private String identificador;
	@Column(name = "DATA_PUBLICACAO")
	private LocalDate dataPublicacao;

	@ManyToOne(fetch = FetchType.LAZY)
	private Categoria categoria;

	@ManyToOne(fetch = FetchType.LAZY)
	private Autor autor;

	@Deprecated
	public Livro() {
	}

	public Livro(String titulo, String resumo, String sumario, BigDecimal preco, Integer paginas, String identificador,
			LocalDate dataPublicacao, Categoria categoria, Autor autor) {
		this.titulo = titulo;
		this.resumo = resumo;
		this.sumario = sumario;
		this.preco = preco;
		this.paginas = paginas;
		this.identificador = identificador;
		this.dataPublicacao = dataPublicacao;
		this.categoria = categoria;
		this.autor = autor;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getTitulo() {
		return titulo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Livro other = (Livro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}