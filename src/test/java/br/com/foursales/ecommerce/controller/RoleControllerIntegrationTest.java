package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.AbstractIntegrationTestSupport;
import br.com.foursales.ecommerce.dto.RoleDto;
import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoleControllerIntegrationTest extends AbstractIntegrationTestSupport {

	@BeforeEach
	public void setUp() {
		usuarioRepository.deleteAll();
		roleRepository.deleteAll();
	}

	private Usuario createAdmin() {
		Role roleAdmin = new Role();
		roleAdmin.setNome("ADMIN");
		roleRepository.save(roleAdmin);

		Usuario usuario = new Usuario();
		usuario.setNome("ADMIN");
		usuario.setEmail("admin@email.com");
		usuario.setSenha("admin_password");
		usuario.setRoles(Set.of(roleAdmin));
		usuarioService.save(usuario);

		return usuario;
	}

	private Usuario createUser() {
		Role roleUser = new Role();
		roleUser.setNome("USER");
		roleRepository.save(roleUser);

		Usuario usuario = new Usuario();
		usuario.setNome("USER");
		usuario.setEmail("user@email.com");
		usuario.setSenha("user_password");
		usuario.setRoles(Set.of(roleUser));
		usuarioService.save(usuario);

		return usuario;
	}

	@Test
	void deveCriarRole() {
		createAdmin();
		String token = getAuthenticatedTokenAdmin();
		RoleDto novaRole = new RoleDto(null,"USER");
		MvcResult saveResult = doRequest("POST", "/role", novaRole, token);
		assertEquals(201, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoCriarRoleSemAutorizacao() {
		RoleDto novaRole = new RoleDto(null,"USER");
		MvcResult saveResult = doRequest("POST", "/role", novaRole);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoCriarRoleSemAutorizacaoComUsuer() {
		createUser();
		String token = getAuthenticatedTokenUser();
		RoleDto novaRole = new RoleDto(null,"ADMIN");
		MvcResult saveResult = doRequest("POST", "/role", novaRole, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveAtualizarRole() {
		createAdmin();
		String token = getAuthenticatedTokenAdmin();

		Role role = new Role();
		role.setNome("GUEST");
		roleRepository.save(role);

		UUID id = roleRepository.findAll().stream().filter(r -> r.getNome().equals("GUEST")).findFirst().get().getId();
		RoleDto roleAtualizada = new RoleDto(id,"USER");

		MvcResult saveResult = doRequest("PUT", "/role/" + id, roleAtualizada, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoAtualizarSemAutorizacao() {
		createUser();
		String token = getAuthenticatedTokenUser();

		Role role = new Role();
		role.setNome("GUEST");
		roleRepository.save(role);

		UUID id = roleRepository.findAll().stream().filter(r -> r.getNome().equals("GUEST")).findFirst().get().getId();
		RoleDto roleAtualizada = new RoleDto(id,"ADMIN");

		MvcResult saveResult = doRequest("PUT", "/role/" + id, roleAtualizada, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoAtualizarSemAutorizacaoComUsuer() {
		Role role = new Role();
		role.setNome("GUEST");
		roleRepository.save(role);

		UUID id = roleRepository.findAll().stream().filter(r -> r.getNome().equals("GUEST")).findFirst().get().getId();
		RoleDto roleAtualizada = new RoleDto(id,"ADMIN");

		MvcResult saveResult = doRequest("PUT", "/role/" + id, roleAtualizada);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveDeletarRole() {
		createAdmin();
		String token = getAuthenticatedTokenAdmin();

		Role role = new Role();
		role.setNome("GUEST");
		roleRepository.save(role);

		UUID id = roleRepository.findAll().stream().filter(r -> r.getNome().equals("GUEST")).findFirst().get().getId();

		MvcResult saveResult = doRequest("DELETE", "/role/" + id, null, token);
		assertEquals(204, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoDeletarSemAutorizacao() {
		Role role = new Role();
		role.setNome("GUEST");
		roleRepository.save(role);

		UUID id = roleRepository.findAll().stream().filter(r -> r.getNome().equals("GUEST")).findFirst().get().getId();

		MvcResult saveResult = doRequest("DELETE", "/role/" + id, null);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoDeletarSemAutorizacaoComUsuer() {
		createUser();
		String token = getAuthenticatedTokenUser();

		Role role = new Role();
		role.setNome("GUEST");
		roleRepository.save(role);

		UUID id = roleRepository.findAll().stream().filter(r -> r.getNome().equals("GUEST")).findFirst().get().getId();

		MvcResult saveResult = doRequest("DELETE", "/role/" + id, null, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveBuscarRole() {
		createAdmin();
		String token = getAuthenticatedTokenAdmin();

		UUID id = roleRepository.findAll().getFirst().getId();

		MvcResult saveResult = doRequest("GET", "/role/" + id, null, token);

		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoBuscarSemAutorizacao() {
		createAdmin();

		UUID id = roleRepository.findAll().getFirst().getId();

		MvcResult saveResult = doRequest("GET", "/role/" + id, null);

		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoBuscarSemAutorizacaoComUsuer() {
		createUser();
		String token = getAuthenticatedTokenUser();

		UUID id = roleRepository.findAll().getFirst().getId();

		MvcResult saveResult = doRequest("GET", "/role/" + id, null, token);

		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveListarRole() {
		createAdmin();
		String token = getAuthenticatedTokenAdmin();

		MvcResult saveResult = doRequest("GET", "/role", null, token);

		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoListarSemAutorizacao() {
		MvcResult saveResult = doRequest("GET", "/role", null, null);

		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoListarSemAutorizacaoComUsuer() {
		createUser();
		String token = getAuthenticatedTokenUser();

		MvcResult saveResult = doRequest("GET", "/role", null, token);

		assertEquals(403, saveResult.getResponse().getStatus());
	}

}
