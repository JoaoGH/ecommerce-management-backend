package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UsuarioService extends DefaultCrudService<Usuario, UUID> {

	private final UsuarioRepository repository;
	private final PasswordEncoder encoder;

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
			Pattern.CASE_INSENSITIVE
	);

	@Override
	protected JpaRepository<Usuario, UUID> getRepository() {
		return repository;
	}

	@Override
	protected void beforeSave(Usuario entity) {
		beforeSaveAndUpdate(entity);
	}

	@Override
	protected void beforeUpdate(Usuario entity) {
		beforeSaveAndUpdate(entity);
	}

	protected void beforeSaveAndUpdate(Usuario entity) {
		validatePassword(entity);

		validateEmail(entity);

		validateRoles(entity);
	}

	private void validatePassword(Usuario entity) {
		if (entity.getSenha() == null || entity.getSenha().isEmpty()) {
			throw new IllegalArgumentException("Senha deve ser preenchida.");
		}

		entity.setSenha(encoder.encode(entity.getSenha()));
	}

	private void validateEmail(Usuario entity) {
		String email = entity.getEmail().toLowerCase();

		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("E-mail deve ser preenchido.");
		}

		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException("E-mail inválido.");
		}

		entity.setEmail(email);

		repository.findByEmail(entity.getEmail()).ifPresent(usuario -> {
			if (!usuario.getId().equals(entity.getId())) {
				throw new IllegalArgumentException("E-mail já em uso.");
			}
		});
	}

	private void validateRoles(Usuario entity) {
		if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
			throw new IllegalArgumentException("Necessario haver ao menois uma Role.");
		}
	}

	public Usuario findByEmail(String email) {
		return repository.findByEmail(email).orElse(null);
	}

}
