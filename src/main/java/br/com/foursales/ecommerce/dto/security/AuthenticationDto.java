package br.com.foursales.ecommerce.dto.security;

public record AuthenticationDto(
		String email,
		String senha
) {
}
