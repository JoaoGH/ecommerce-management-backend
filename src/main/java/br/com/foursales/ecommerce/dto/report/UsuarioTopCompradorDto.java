package br.com.foursales.ecommerce.dto.report;

import java.math.BigDecimal;
import java.util.UUID;

public record UsuarioTopCompradorDto(
		UUID id,
		String nome,
		BigDecimal totalComprado
) {
}
