package br.com.foursales.ecommerce.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponseDto(
		UUID id,
		String nome,
		String descricao,
		BigDecimal preco,
		String categoria,
		Integer quantidadeEmEstoque
) {
}
