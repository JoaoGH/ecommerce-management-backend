package br.com.foursales.ecommerce.service.security;

import br.com.foursales.ecommerce.dto.security.AuthenticationDto;
import br.com.foursales.ecommerce.dto.security.AuthenticationResponseDto;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class SecurityService {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final UsuarioRepository usuarioRepository;

	public Usuario getCurentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
			return (Usuario) authentication.getPrincipal();
		}

		return null;
	}

	public AuthenticationResponseDto doLogin(AuthenticationDto dto) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

		authenticationManager.authenticate(authentication);

		Usuario usuario = usuarioRepository.findByEmail(dto.email()).get();

		String token = tokenService.generateToken(usuario);

		LocalDateTime expiresAt = JWT.decode(token).getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		return new AuthenticationResponseDto(usuario.getEmail(), token, LocalDateTime.now(), expiresAt);
	}



}
