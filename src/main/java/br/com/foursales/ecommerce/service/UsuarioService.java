package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.exception.DefaultApiException;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
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
			throw new DefaultApiException("Senha deve ser preenchida.", HttpStatus.BAD_REQUEST);
		}

		entity.setSenha(encoder.encode(entity.getSenha()));
	}

	private void validateEmail(Usuario entity) {
		String email = entity.getEmail().toLowerCase();

		if (email == null || email.isBlank()) {
			throw new DefaultApiException("E-mail deve ser preenchido.", HttpStatus.BAD_REQUEST);
		}

		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new DefaultApiException("E-mail inválido.", HttpStatus.BAD_REQUEST);
		}

		entity.setEmail(email);

		repository.findByEmail(entity.getEmail()).ifPresent(usuario -> {
			if (!usuario.getId().equals(entity.getId())) {
				throw new DefaultApiException("E-mail já em uso.", HttpStatus.UNPROCESSABLE_ENTITY);
			}
		});
	}

	private void validateRoles(Usuario entity) {
		if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
			throw new DefaultApiException("Necessario haver ao menos uma Role.", HttpStatus.BAD_REQUEST);
		}
	}

}
