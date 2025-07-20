package br.com.foursales.ecommerce.repository;


import br.com.foursales.ecommerce.dto.report.UsuarioTicketMedioDto;
import br.com.foursales.ecommerce.dto.report.UsuarioTopCompradorDto;
import br.com.foursales.ecommerce.entity.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

	Optional<Usuario> findByEmail(String email);

	@Query("""
    SELECT new br.com.foursales.ecommerce.dto.report.UsuarioTopCompradorDto(
        u.id,
        u.nome,
        CAST(SUM(i.quantidade * i.precoUnitario) AS bigdecimal)
    )
    FROM Usuario u
    JOIN Pedido p ON p.usuario.id = u.id
    JOIN PedidoItem i ON i.pedido.id = p.id
    GROUP BY u.id, u.nome
    ORDER BY SUM(i.quantidade * i.precoUnitario) DESC
    """)
	List<UsuarioTopCompradorDto> findTop5UsuariosMaisCompraram(Pageable pageable);

	@Query("""
    SELECT new br.com.foursales.ecommerce.dto.report.UsuarioTicketMedioDto(
        u.id,
        u.nome,
        CAST(AVG(p.valorTotal) as bigdecimal)
    )
    FROM Usuario u
    JOIN Pedido p ON p.usuario.id = u.id
    GROUP BY u.id, u.nome
    """)
	List<UsuarioTicketMedioDto> calcularTicketMedioPorUsuario();

}
