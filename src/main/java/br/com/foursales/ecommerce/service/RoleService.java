package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.enums.RolesPermitidas;
import br.com.foursales.ecommerce.exception.DefaultApiException;
import br.com.foursales.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService extends DefaultCrudService<Role, UUID> {

	private final RoleRepository repository;

	@Override
	protected JpaRepository<Role, UUID> getRepository() {
		return repository;
	}

	@Override
	protected void beforeSave(Role entity) {
		if (entity.getNome() == null || entity.getNome().isBlank()) {
			throw new DefaultApiException("O nome da role não pode ser nulo ou vazio.", HttpStatus.BAD_REQUEST);
		}

		entity.setNome(entity.getNome().toUpperCase());

		boolean nomeValido = Arrays.stream(RolesPermitidas.values())
				.anyMatch(r -> r.name().equals(entity.getNome()));

		if (!nomeValido) {
			throw new DefaultApiException("Nome de role inválido: " + entity.getNome(), HttpStatus.BAD_REQUEST);
		}
	}
}
