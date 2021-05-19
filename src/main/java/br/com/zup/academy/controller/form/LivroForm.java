package br.com.zup.academy.controller.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import br.com.zup.academy.dominio.modelo.Autor;
import br.com.zup.academy.dominio.modelo.Categoria;
import br.com.zup.academy.dominio.modelo.Livro;
import br.com.zup.academy.dominio.validator.anotacao.ExistsId;
import br.com.zup.academy.dominio.validator.anotacao.UniqueValue;

public class LivroForm {

	@NotBlank
	@UniqueValue(fieldName = "titulo", domainClass = Livro.class)
	private String titulo;
	@NotBlank
	@Size(max = 500)
	private String resumo;
	@NotBlank
	private String sumario;
	@NotNull
	@Min(20)
	private BigDecimal preco;
	@NotNull
	@Min(100)
	private Integer paginas;
	@NotBlank
	@UniqueValue(fieldName = "identificador", domainClass = Livro.class)
	private String identificador;
	@Future
	@JsonFormat(pattern = "dd/MM/yyyy", shape = Shape.STRING)
	private LocalDate dataPublicacao;
	@NotNull
	@ExistsId(domainClass = Categoria.class, field = "id")
	private Long categoriaId;
	@NotNull
	@ExistsId(domainClass = Autor.class, field = "id")
	private Long autorId;

	@JsonCreator
	public LivroForm(@NotBlank String titulo, @NotBlank @Size(max = 500) String resumo, @NotBlank String sumario,
			@NotNull @Min(20) BigDecimal preco, @NotNull @Min(100) Integer paginas, @NotBlank String identificador,
			@Future LocalDate dataPublicacao, @NotNull Long categoriaId, @NotNull Long autorId) {
		super();
		this.titulo = titulo;
		this.resumo = resumo;
		this.sumario = sumario;
		this.preco = preco;
		this.paginas = paginas;
		this.identificador = identificador;
		this.dataPublicacao = dataPublicacao;
		this.categoriaId = categoriaId;
		this.autorId = autorId;
	}

	public Livro toLivro(EntityManager manager) {
		@NotNull
		Categoria categoria = manager.find(Categoria.class, this.categoriaId);
		@NotNull
		Autor autor = manager.find(Autor.class, this.autorId);
		return new Livro(this.titulo, this.resumo, this.sumario, this.preco, this.paginas, this.identificador,
				this.dataPublicacao, categoria, autor);
	}
}