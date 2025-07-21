package br.com.foursales.ecommerce.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoItemResponseDto(
		ProdutoResponseDto produto,
		Integer quantidade,
		BigDecimal precoUnitario
) {
}
