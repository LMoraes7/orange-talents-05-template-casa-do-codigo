package br.com.zup.academy.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.zup.academy.dominio.modelo.Livro;

public class LivroDetalhadoDto {

	private Long id;
	private String titulo;
	private String resumo;
	private String sumario;
	private BigDecimal preco;
	private Integer paginas;
	private String identificador;
	private LocalDate dataPublicacao;
	private AutorDto autor;

	public LivroDetalhadoDto(Livro livro) {
		this.id = livro.getId();
		this.titulo = livro.getTitulo();
		this.resumo = livro.getResumo();
		this.sumario = livro.getSumario();
		this.preco = livro.getPreco();
		this.paginas = livro.getPaginas();
		this.identificador = livro.getIdentificador();
		this.dataPublicacao = livro.getDataPublicacao();
		this.autor = new AutorDto(livro.getAutor());
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getResumo() {
		return resumo;
	}

	public String getSumario() {
		return sumario;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public Integer getPaginas() {
		return paginas;
	}

	public String getIdentificador() {
		return identificador;
	}

	public LocalDate getDataPublicacao() {
		return dataPublicacao;
	}

	public AutorDto getAutor() {
		return autor;
	}
}