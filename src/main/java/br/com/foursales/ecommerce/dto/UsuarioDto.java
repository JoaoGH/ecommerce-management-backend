package br.com.foursales.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UsuarioDto(
	UUID id,
	@NotBlank(message = "Campo obrigatório")
	@Size(min = 3, max = 255, message = "Fora do tamanho definido de 3 a 255 caracteres")
	String nome,
	@NotBlank(message = "Campo obrigatório")
	@Size(min = 10, max = 255, message = "Fora do tamanho definido de 10 a 255 caracteres")
	@Email(message = "E-mail inválido")
	String email,
	@NotBlank(message = "Campo obrigatório")
	String senha,
	@NotBlank(message = "Campo obrigatório")
	List<String> roles
) {
}
