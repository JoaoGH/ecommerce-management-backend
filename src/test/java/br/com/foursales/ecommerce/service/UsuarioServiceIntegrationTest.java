package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.exception.DefaultApiException;
import br.com.foursales.ecommerce.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceIntegrationTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		usuarioRepository.deleteAll();
		roleRepository.deleteAll();

		Role roleUser = new Role();
		roleUser.setNome("USER");

		Role roleAdmin = new Role();
		roleAdmin.setNome("ADMIN");

		roleRepository.saveAll(List.of(roleUser, roleAdmin));

		Usuario user = new Usuario();
		user.setNome("Usuário");
		user.setEmail("user@email.com");
		user.setSenha("senha123");
		user.setRoles(Set.of(roleUser));

		Usuario admin = new Usuario();
		admin.setNome("Administrador");
		admin.setEmail("adm@email.com");
		admin.setSenha("senha321");
		admin.setRoles(Set.of(roleAdmin));

		usuarioRepository.saveAll(List.of(user, admin));
	}

	@Test
	public void deveCriarUsuarioComDadosValidos() {
		Role role = roleRepository.findByNome("USER").orElseThrow();

		Usuario novo = new Usuario();
		novo.setNome("Novo Usuário");
		novo.setEmail("novo_user@email.com");
		novo.setSenha("senha");
		novo.setRoles(Set.of(role));

		assertDoesNotThrow(() -> usuarioService.save(novo));
	}

	@Test
	public void naoDeveCriarUsuarioComEmailDuplicado() {
		Role role = roleRepository.findByNome("USER").orElseThrow();

		Usuario novo = new Usuario();
		novo.setNome("Email Duplicado");
		novo.setEmail("user@email.com");
		novo.setSenha("duplicado");
		novo.setRoles(Set.of(role));

		assertThrows(DefaultApiException.class, () -> {
			usuarioService.save(novo);
		});
	}

	@Test
	public void naoDeveCriarUsuarioComSenhaVaziaOuNula() {
		Role role = roleRepository.findByNome("USER").orElseThrow();

		Usuario novo = new Usuario();
		novo.setNome("Senha vazia");
		novo.setEmail("senha_vazia@email.com");
		novo.setRoles(Set.of(role));

		assertThrows(DefaultApiException.class, () -> {
			usuarioService.save(novo);
		});

		novo.setSenha("");
		assertThrows(DefaultApiException.class, () -> {
			usuarioService.save(novo);
		});
	}

	@Test
	public void naoDeveCriarUsuarioComEmailInvalido() {
		Role role = roleService.list().getFirst();
		Usuario novo = new Usuario();
		novo.setNome("Email Invalido");
		novo.setEmail("usuário com!?email invlaido@@email.com.");
		novo.setSenha("invalido");
		novo.setRoles(Set.of(role));

		assertThrows(DefaultApiException.class, () -> {
			usuarioService.save(novo);
		});
	}

	@Test
	public void naoDeveCriarUsuarioSemRoles() {
		Usuario novo = new Usuario();
		novo.setNome("Sem Roles");
		novo.setEmail("sem_roles@email.com");
		novo.setSenha("senha");

		assertThrows(DefaultApiException.class, () -> {
			usuarioService.save(novo);
		});
	}

	@Test
	public void deveBuscarUsuarioPorId() {
		List<Usuario> usuarios = usuarioRepository.findAll();

		assertNotNull(usuarios);
		assertFalse(usuarios.isEmpty());

		UUID id = usuarios.getFirst().getId();
		Usuario foundedById = usuarioService.get(id);

		assertNotNull(foundedById);
	}

	@Test
	public void deveAtualizarDadosDoUsuario() {
		List<Usuario> usuarios = usuarioRepository.findAll();

		assertNotNull(usuarios);
		assertFalse(usuarios.isEmpty());

		Usuario usuario = usuarios.getFirst();
		usuario.setNome("Nome atualizado");

		assertDoesNotThrow(() -> usuarioService.update(usuario.getId(), usuario));
	}

	@Test
	public void naoDeveAtualizarParaEmailJaUsado() {
		List<Usuario> usuarios = usuarioRepository.findAll();

		assertNotNull(usuarios);
		assertFalse(usuarios.isEmpty());
		assertEquals(2, usuarios.size());

		Usuario usuario = usuarios.getFirst();
		usuario.setNome("Nome atualizado");
		usuario.setEmail(usuarios.get(1).getEmail());

		assertThrows(DefaultApiException.class, () -> {
			usuarioService.update(usuario.getId(), usuario);
		});
	}

	@Test
	public void deveExcluirUsuarioPorId() {
		List<Usuario> usuarios = usuarioRepository.findAll();

		assertNotNull(usuarios);
		assertFalse(usuarios.isEmpty());

		UUID id = usuarios.getFirst().getId();

		assertDoesNotThrow(() -> usuarioService.delete(id));
	}

	@Test
	public void naoDeveExcluirUsuarioInexistente() {
		UUID id = UUID.randomUUID();
		while (usuarioService.get(id) != null) {
			id = UUID.randomUUID();
		}

		UUID finalId = id;
		assertThrows(EntityNotFoundException.class, () -> {
			usuarioService.delete(finalId);
		});

		assertThrows(DefaultApiException.class, () -> {
			usuarioService.delete(null);
		});
	}

	@Test
	public void deveRetornarUsuarioPorEmail() {
		Usuario usuario = usuarioRepository.findByEmail("adm@email.com").orElse(null);

		assertNotNull(usuario);
	}

	@Test
	public void deveCriptografarSenhaAoSalvarUsuario() {
		Role role = roleRepository.findByNome("USER").orElseThrow();

		Usuario usuario = new Usuario();
		usuario.setNome("Novo Usuário");
		usuario.setEmail("usuario_user@email.com");
		usuario.setSenha("senha");
		usuario.setRoles(Set.of(role));

		usuario = usuarioService.save(usuario);

		assertNotEquals("senha", usuario.getSenha());
		assertTrue(passwordEncoder.matches("senha", usuario.getSenha()));
	}

}
