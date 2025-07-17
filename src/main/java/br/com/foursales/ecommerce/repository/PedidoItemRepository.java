package br.com.foursales.ecommerce.repository;

import br.com.foursales.ecommerce.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}
