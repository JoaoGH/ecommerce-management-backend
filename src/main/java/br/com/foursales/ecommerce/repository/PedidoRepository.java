package br.com.foursales.ecommerce.repository;

import br.com.foursales.ecommerce.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}
