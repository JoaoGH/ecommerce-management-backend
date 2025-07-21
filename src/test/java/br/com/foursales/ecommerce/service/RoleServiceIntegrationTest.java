package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.exception.DefaultApiException;
import br.com.foursales.ecommerce.repository.RoleRepository;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class RoleServiceIntegrationTest {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeEach
	public void setUp() {
		usuarioRepository.deleteAll();
		roleRepository.deleteAll();

		Role admin = new Role();
		admin.setNome("ADMIN");

		Role user = new Role();
		user.setNome("USER");

		roleRepository.saveAll(List.of(admin, user));
	}

	@Test
	public void deveBuscarRolePorNome() {
		Role role = roleRepository.findByNome("ADMIN").orElse(null);

		assertNotNull(role);
		assertTrue(role.getNome().equals("ADMIN"));
	}

	@Test
	public void naoDeveRetornarRoleComNomeInvalido() {
		Role resultado = roleRepository.findByNome("ROLE_NAO_RECONHECIDA").orElse(null);

		assertNull(resultado);
	}

	@Test
	public void deveLancarExcecaoDuplicacaoDeNomeAoSalvar() {
		Role nova = new Role();
		nova.setNome("ADMIN");

		assertThrows(RuntimeException.class, () -> {
			roleService.save(nova);
		});
	}

	@Test
	public void deveLancarExcecaoSeRoleNaoReconhecidaAoSalvar() {
		Role nova = new Role();
		nova.setNome("ROLE_NAO_RECONHECIDA");

		assertThrows(RuntimeException.class, () -> {
			roleService.save(nova);
		});
	}

	@Test
	public void deveLancarExcecaoSeRoleSemNomeAoSalvar() {
		Role nova = new Role();

		assertThrows(RuntimeException.class, () -> {
			roleService.save(nova);
		});
	}

	@Test
	public void deveLancarExcecaoSeRoleNomeVazioAoSalvar() {
		Role nova = new Role();
		nova.setNome("");

		assertThrows(RuntimeException.class, () -> {
			roleService.save(nova);
		});
	}

	@Test
	public void deveRetornarTodasAsRoles() {
		List<Role> roles = roleService.list();

		assertEquals(2, roles.size());
		assertTrue(roles.stream().anyMatch(r -> r.getNome().equals("ADMIN")));
		assertTrue(roles.stream().anyMatch(r -> r.getNome().equals("USER")));
	}

	@Test
	public void deveBuscarRolePorId() {
		List<Role> roles = roleService.list();

		assertNotNull(roles);
		assertEquals(2, roles.size());

		UUID id = roles.getFirst().getId();
		Role role = roleService.get(id);

		assertNotNull(role);
	}

	@Test
	public void deveExcluirRolePorId() {
		List<Role> roles = roleService.list();

		assertNotNull(roles);
		assertEquals(2, roles.size());

		UUID id = roles.getFirst().getId();
		assertDoesNotThrow(() -> roleService.delete(id));
	}

	@Test
	public void deveLancarExcecaoSeRoleInexistenteAoExcluir() {
		UUID id = UUID.randomUUID();
		while (roleService.get(id) != null) {
			id = UUID.randomUUID();
		}

		UUID finalId = id;
		assertThrows(EntityNotFoundException.class, () -> {
			roleService.delete(finalId);
		});
	}

	@Test
	public void deveLancarExcecaoSeIdNuloAoExcluir() {
		assertThrows(DefaultApiException.class, () -> {
			roleService.delete(null);
		});
	}

	@Test
	public void deveLancarExcecaoSeRoleNaoExisteAoAtualizar() {
		UUID id = UUID.randomUUID();
		while (roleService.get(id) != null) {
			id = UUID.randomUUID();
		}

		UUID finalId = id;

		Role nova = new Role();
		nova.setId(finalId);
		assertThrows(EntityNotFoundException.class, () -> {
			roleService.update(nova.getId(), nova);
		});
	}

	@Test
	public void deveLancarExcecaoSeIdNuloAoAtualizar() {
		Role nova = new Role();

		assertThrows(DefaultApiException.class, () -> {
			roleService.update(nova.getId(), nova);
		});
	}

}
