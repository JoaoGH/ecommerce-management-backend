package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UsuarioService extends DefaultCrudService<Usuario, UUID> {

	private final UsuarioRepository repository;

	@Override
	protected JpaRepository<Usuario, UUID> getRepository() {
		return repository;
	}

}
