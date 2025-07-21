package br.com.foursales.ecommerce.dto;

import java.util.UUID;

public record RoleResponseDto(
		UUID id,
		String nome
) {
}
