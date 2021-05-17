package br.zom.zup.academy.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.zom.zup.academy.dominio.modelo.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long>{

}
