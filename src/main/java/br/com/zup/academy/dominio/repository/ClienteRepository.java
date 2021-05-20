package br.com.zup.academy.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.academy.dominio.modelo.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
