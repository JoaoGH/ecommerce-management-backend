package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.AbstractIntegrationTestSupport;
import br.com.foursales.ecommerce.dto.UsuarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerIntegrationTest extends AbstractIntegrationTestSupport {

	@BeforeEach
	public void setUp() {
		createUsers();
	}

	UsuarioDto createUsuarioDto() {
		return new UsuarioDto(null, "Novo Usuário", "teste@email.com", "senha123", List.of("ADMIN"));
	}


	@Test
	public void deveCriarUsuarioComAdmin() {
		String token = getAuthenticatedTokenAdmin();

		MvcResult saveResult = doRequest("POST", "/usuario", createUsuarioDto(), token);
		assertEquals(201, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoCriarUsuarioSemAutorizacao() {
		MvcResult saveResult = doRequest("POST", "/usuario", createUsuarioDto());
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoCriarUsuarioSemAutorizacaoComUsuer() {
		String token = getAuthenticatedTokenUser();

		MvcResult saveResult = doRequest("POST", "/usuario", createUsuarioDto(), token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveAtualizarUsuarioComAdmin() {
		String token = getAuthenticatedTokenAdmin();

		UUID id = usuarioRepository.findAll().get(0).getId();
		UsuarioDto usuarioDto = new UsuarioDto(id, "Nome atualizado", "atualizado@email.com", "senha_nova", List.of("ADMIN"));

		MvcResult saveResult = doRequest("PUT", "/usuario/" + id, usuarioDto, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoAtualizarUsuarioSemAutorizacao() {
		UUID id = usuarioRepository.findAll().get(0).getId();
		UsuarioDto usuarioDto = new UsuarioDto(id, "Nome atualizado", "atualizado@email.com", "senha_nova", List.of("ADMIN"));

		MvcResult saveResult = doRequest("PUT", "/usuario/" + id, usuarioDto);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoAtualizarUsuarioSemAutorizacaoComUsuer() {
		String token = getAuthenticatedTokenUser();

		UUID id = usuarioRepository.findAll().get(0).getId();
		UsuarioDto usuarioDto = new UsuarioDto(id, "Nome atualizado", "atualizado@email.com", "senha_nova", List.of("ADMIN"));

		MvcResult saveResult = doRequest("PUT", "/usuario/" + id, usuarioDto, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveDeletarUsuarioComAdmin() {
		String token = getAuthenticatedTokenAdmin();

		UUID id = usuarioRepository.findAll().get(0).getId();

		MvcResult saveResult = doRequest("DELETE", "/usuario/" + id, null, token);
		assertEquals(204, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoDeletarUsuarioSemAutorizacao() {
		UUID id = usuarioRepository.findAll().get(0).getId();

		MvcResult saveResult = doRequest("DELETE", "/usuario/" + id, null);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoDeletarUsuarioSemAutorizacaoComUsuer() {
		String token = getAuthenticatedTokenUser();

		UUID id = usuarioRepository.findAll().get(0).getId();

		MvcResult saveResult = doRequest("DELETE", "/usuario/" + id, null, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveBuscarUsuarioComAdmin() {
		String token = getAuthenticatedTokenAdmin();

		UUID id = usuarioRepository.findAll().get(0).getId();

		MvcResult saveResult = doRequest("GET", "/usuario/" + id, null, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoBuscarUsuarioSemAutorizacao() {
		UUID id = usuarioRepository.findAll().get(0).getId();

		MvcResult saveResult = doRequest("GET", "/usuario/" + id, null);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoBuscarUsuarioSemAutorizacaoComUsuer() {
		String token = getAuthenticatedTokenUser();

		UUID id = usuarioRepository.findAll().get(0).getId();

		MvcResult saveResult = doRequest("GET", "/usuario/" + id, null, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveListarUsuarioComAdmin() {
		String token = getAuthenticatedTokenAdmin();

		MvcResult saveResult = doRequest("GET", "/usuario", null, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoListarUsuarioSemAutorizacao() {
		MvcResult saveResult = doRequest("GET", "/usuario", null);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403AoListarUsuarioSemAutorizacaoComUsuer() {
		String token = getAuthenticatedTokenUser();

		MvcResult saveResult = doRequest("GET", "/usuario", null, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

}
