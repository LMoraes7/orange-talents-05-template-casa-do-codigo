package br.com.zup.academy.controller.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.zup.academy.dominio.modelo.Autor;
import br.com.zup.academy.dominio.modelo.Categoria;
import br.com.zup.academy.dominio.modelo.Livro;
import br.com.zup.academy.dominio.repository.AutorRepository;
import br.com.zup.academy.dominio.repository.CategoriaRepository;
import br.com.zup.academy.dominio.validator.anotacao.DataFutureValid;
import br.com.zup.academy.dominio.validator.anotacao.ExistsEntity;
import br.com.zup.academy.dominio.validator.anotacao.PrecoValid;
import br.com.zup.academy.dominio.validator.anotacao.UniqueValue;

public class LivroForm {

	@UniqueValue(fieldName = "titulo", domainClass = Livro.class)
	private String titulo;
	@NotBlank
	@Size(max = 500)
	private String resumo;
	@NotBlank
	private String sumario;
	@PrecoValid(min = 20)
	private String preco;
	@NotNull
	@Min(100)
	private Integer paginas;
	@UniqueValue(fieldName = "identificador", domainClass = Livro.class)
	private String identificador;
	@DataFutureValid
	private String dataPublicacao;
	@ExistsEntity(domainClass = Categoria.class)
	private Long categoriaId;
	@ExistsEntity(domainClass = Autor.class)
	private Long autorId;

	@JsonCreator
	public LivroForm(@NotBlank String titulo, @NotBlank @Size(max = 500) String resumo, @NotEmpty String sumario,
			String preco, @NotNull @Min(100) Integer paginas, @NotBlank String identificador, String dataPublicacao,
			Long categoriaId, Long autorId) {
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

	public Livro toLivro(CategoriaRepository categoriaRepository, AutorRepository autorRepository) {
		Categoria categoria = categoriaRepository.findById(this.categoriaId).get();
		Autor autor = autorRepository.findById(this.autorId).get();
		return new Livro(this.titulo, this.resumo, this.sumario, new BigDecimal(this.preco), this.paginas,
				this.identificador, LocalDate.parse(this.dataPublicacao), categoria, autor);
	}
}