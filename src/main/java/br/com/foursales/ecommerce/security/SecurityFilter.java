package br.com.foursales.ecommerce.security;

import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import br.com.foursales.ecommerce.service.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = recoverToken(request);

		if (token != null) {
			String subject = tokenService.validateToken(token);
			Usuario usuario = usuarioRepository.findByEmail(subject).orElse(null);

			List<GrantedAuthority> grantedAuthorities = usuario.getRoles()
					.stream()
					.map(role -> new SimpleGrantedAuthority(role.getNome()))
					.collect(Collectors.toList());

			Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, grantedAuthorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String recoverToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		return authHeader == null ? null : authHeader.replace("Bearer ", "");
	}

}
