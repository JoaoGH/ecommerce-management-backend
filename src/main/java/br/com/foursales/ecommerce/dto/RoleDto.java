package br.com.foursales.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RoleDto(
		UUID id,
		@NotBlank(message = "Campo obrigatório")
		@Size(min = 1, max = 50, message = "Fora do tamanho definido de 1 a 50 caracteres")
		String nome
) {
}
