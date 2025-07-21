package br.com.foursales.ecommerce.dto;

import java.util.UUID;

public record PedidoItemDto(
		UUID idPedido,
		UUID idProduto,
		Integer quantidade
) {
}
