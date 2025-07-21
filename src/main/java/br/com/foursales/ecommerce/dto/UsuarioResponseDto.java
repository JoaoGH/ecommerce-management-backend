package br.com.foursales.ecommerce.dto;

import java.util.List;
import java.util.UUID;

public record UsuarioResponseDto(
		UUID id,
		String nome,
		String email,
		List<RoleResponseDto> roles
) {
}
