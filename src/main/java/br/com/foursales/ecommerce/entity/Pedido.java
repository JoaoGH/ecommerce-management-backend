package br.com.foursales.ecommerce.entity;

import br.com.foursales.ecommerce.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido extends AuditableEntity implements Identifiable<UUID> {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusPedido status;

	@Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorTotal;

	private String observacao;

}
