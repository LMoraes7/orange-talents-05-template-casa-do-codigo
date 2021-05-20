package br.com.zup.academy.dominio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.academy.dominio.modelo.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long>{

	Optional<Estado> findByNomeAndPaisId(String nome, Long id);

	Optional<Estado> findByIdAndPaisId(Long estadoId, Long paisId);
}
