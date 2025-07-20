package br.com.foursales.ecommerce.repository;

import br.com.foursales.ecommerce.dto.report.FaturamentoMensalDto;
import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

	List<Pedido> findAllByUsuario(Usuario usuario);

	@Query("""
    SELECT new br.com.foursales.ecommerce.dto.report.FaturamentoMensalDto(
        cast(COALESCE(SUM(p.valorTotal), 0) as bigdecimal)
    )
    FROM Pedido p
    WHERE MONTH(p.dataCadastro) = MONTH(CURRENT_DATE)
      AND YEAR(p.dataCadastro) = YEAR(CURRENT_DATE)
    """)
	FaturamentoMensalDto calcularFaturamentoMensal();


}
