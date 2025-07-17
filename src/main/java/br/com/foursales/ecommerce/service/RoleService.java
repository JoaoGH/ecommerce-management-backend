package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService extends DefaultCrudService<Role, UUID> {

	private final RoleRepository repository;

	@Override
	protected JpaRepository<Role, UUID> getRepository() {
		return repository;
	}
}
