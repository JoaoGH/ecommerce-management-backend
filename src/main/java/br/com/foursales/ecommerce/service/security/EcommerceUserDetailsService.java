package br.com.foursales.ecommerce.service.security;

import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcommerceUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username).orElse(null);

		if (usuario == null) {
			throw new UsernameNotFoundException(username);
		}

		return User.builder()
				.username(usuario.getEmail())
				.password(usuario.getSenha())
				.roles(usuario.getRoles().stream().map(Role::getNome).toArray(String[]::new))
				.build();
	}

}
