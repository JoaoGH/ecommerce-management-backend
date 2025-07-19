package br.com.foursales.ecommerce.dto;

import java.util.UUID;

public record ItemPedidoDTO(
		UUID idPedido,
		UUID idProduto,
		Integer quantidade
) {
}
