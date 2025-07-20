package br.com.foursales.ecommerce.service.security;

import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcommerceUserDetailsService implements UserDetailsService {

	private final UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.findByEmail(username);

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
