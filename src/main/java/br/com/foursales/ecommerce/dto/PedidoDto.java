package br.com.foursales.ecommerce.dto;

import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.enums.StatusPedido;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoDto(
		UUID id,
		Usuario usuario,
		StatusPedido status,
		BigDecimal valorFinal
) {
}
