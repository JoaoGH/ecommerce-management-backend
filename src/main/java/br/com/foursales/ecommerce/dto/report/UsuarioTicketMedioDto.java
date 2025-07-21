package br.com.foursales.ecommerce.dto.report;

import java.math.BigDecimal;
import java.util.UUID;

public record UsuarioTicketMedioDto(
		UUID id,
		String nome,
		BigDecimal ticketMedio
) {
}
