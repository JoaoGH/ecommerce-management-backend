package br.com.foursales.ecommerce.repository;

import br.com.foursales.ecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
