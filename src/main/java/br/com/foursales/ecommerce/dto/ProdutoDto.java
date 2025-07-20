package br.com.foursales.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoDto(
		UUID id,
		@NotBlank(message = "Campo obrigatório")
		@Size(min = 1, max = 255, message = "Fora do tamanho definido de 1 a 255 caracteres")
		String nome,
		@NotBlank(message = "Campo obrigatório")
		String descricao,
		@NotBlank(message = "Campo obrigatório")
		BigDecimal preco,
		@NotBlank(message = "Campo obrigatório")
		@Size(min = 1, max = 50, message = "Fora do tamanho definido de 1 a 50 caracteres")
		String categoria,
		@NotBlank(message = "Campo obrigatório")
		@PositiveOrZero
		Integer quantidadeEmEstoque
) {
}
