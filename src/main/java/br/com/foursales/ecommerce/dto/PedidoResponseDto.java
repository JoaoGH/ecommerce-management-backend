package br.com.foursales.ecommerce.dto;

import br.com.foursales.ecommerce.enums.StatusPedido;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PedidoResponseDto(
		UUID id,
		UsuarioResponseDto usuario,
		StatusPedido status,
		List<PedidoItemResponseDto> itens,
		BigDecimal valorTotal,
		String observacao
) {
}
