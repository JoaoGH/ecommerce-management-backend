package br.com.foursales.ecommerce.repository;

import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

	List<Pedido> findAllByUsuario(Usuario usuario);

}
