package br.com.foursales.ecommerce.service.security;

import br.com.foursales.ecommerce.dto.security.AuthenticationDto;
import br.com.foursales.ecommerce.dto.security.AuthenticationResponseDto;
import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.RoleRepository;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import br.com.foursales.ecommerce.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SecurityServiceIntegrationTest {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private SecurityService securityService;

	@BeforeEach
	public void setUp() {
		usuarioRepository.deleteAll();
		roleRepository.deleteAll();

		Role roleUser = new Role();
		roleUser.setNome("USER");

		roleRepository.save(roleUser);

		Usuario userUser = new Usuario();
		userUser.setNome("USER");
		userUser.setEmail("user@email.com");
		userUser.setSenha("senha");
		userUser.setRoles(Set.of(roleUser));
		usuarioService.save(userUser);
	}

	@Test
	void realizaLogin() {
		AuthenticationDto dto = new AuthenticationDto("user@email.com", "senha");

		AuthenticationResponseDto login = securityService.doLogin(dto);

		assertNotNull(login.token());
	}

	@Test
	void naoRealizaLogin() {
		AuthenticationDto dto = new AuthenticationDto("user@email.com", "senha_errada");

		assertThrows(BadCredentialsException.class, () -> securityService.doLogin(dto));
	}

}
