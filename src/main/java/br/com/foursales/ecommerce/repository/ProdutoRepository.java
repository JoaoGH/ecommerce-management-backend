package br.com.foursales.ecommerce.repository;

import br.com.foursales.ecommerce.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
}
