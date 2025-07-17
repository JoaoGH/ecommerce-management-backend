package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService extends DefaultCrudService<Pedido, UUID> {

	private final PedidoRepository pedidoRepository;

	@Override
	protected JpaRepository<Pedido, UUID> getRepository() {
		return pedidoRepository;
	}
}
