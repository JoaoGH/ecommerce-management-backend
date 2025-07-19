package br.com.foursales.ecommerce.repository;

import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

	List<PedidoItem> findByPedido(Pedido pedido);

}
