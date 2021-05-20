package br.com.zup.academy.dominio.util;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.zup.academy.dominio.modelo.Estado;

public class VerificadorPaisEstado {

	public static boolean paisPossuiEstados(EntityManager manager, Long paisId) {
		List<Estado> list = manager.createQuery("SELECT e FROM Estado e JOIN e.pais p WHERE p.id = :id", Estado.class)
				.setParameter("id", paisId).getResultList();
		return !list.isEmpty();
	}

	public static boolean paisPossuiDeterminadoEstado(EntityManager manager, Long paisId, Long estadoId) {
		if(estadoId == null)
			return false;
		List<Estado> list = manager.createQuery("SELECT e FROM Estado e JOIN e.pais p WHERE e.id = :idEstado AND p.id = :idPais", Estado.class)
			.setParameter("idEstado", estadoId)
			.setParameter("idPais", paisId)
			.getResultList();
		return !list.isEmpty();
	}
}