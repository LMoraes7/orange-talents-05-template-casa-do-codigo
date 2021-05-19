package br.com.zup.academy.dominio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.zup.academy.dominio.modelo.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>{

	@Query("SELECT l FROM Livro l JOIN FETCH l.autor WHERE l.id = :id")
	Optional<Livro> consultarPorId(@Param("id") Long id);
}
