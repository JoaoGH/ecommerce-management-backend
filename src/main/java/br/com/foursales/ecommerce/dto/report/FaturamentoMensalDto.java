package br.com.foursales.ecommerce.dto.report;

import java.math.BigDecimal;

public record FaturamentoMensalDto(
		BigDecimal totalFaturado
) {
}
