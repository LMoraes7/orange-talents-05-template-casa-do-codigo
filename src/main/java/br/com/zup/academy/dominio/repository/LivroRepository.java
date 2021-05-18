package br.com.zup.academy.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.academy.dominio.modelo.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>{

}
