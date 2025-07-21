package br.com.foursales.ecommerce.repository;

import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.entity.PedidoItem;
import br.com.foursales.ecommerce.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

	List<PedidoItem> findByPedido(Pedido pedido);

	PedidoItem findByPedidoAndProduto(Pedido pedido, Produto produto);

	@Query("""
			    SELECT pi.pedido FROM PedidoItem pi 
			    WHERE pi.pedido <> :pedido 
			    AND pi.produto = :produto
			""")
	List<Pedido> findAllByPedidoNotAndProduto(Pedido pedido, Produto produto);
}
