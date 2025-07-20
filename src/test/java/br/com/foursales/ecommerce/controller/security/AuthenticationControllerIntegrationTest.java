package br.com.foursales.ecommerce.controller.security;

import br.com.foursales.ecommerce.AbstractIntegrationTestSupport;
import br.com.foursales.ecommerce.dto.security.AuthenticationDto;
import br.com.foursales.ecommerce.dto.security.AuthenticationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerIntegrationTest extends AbstractIntegrationTestSupport {

	@BeforeEach
	public void setUp() {
		createUsers();
	}

	@Test
	void deveAutenticarComSucesso() {
		AuthenticationDto authDto = new AuthenticationDto("user@email.com", "user_password");

		MvcResult result = doRequest("post", "/auth/login", authDto);
		String responseJson = getContentAsStringFromResponse(result.getResponse());
		Integer status = result.getResponse().getStatus();

		AuthenticationResponseDto responseDto = (AuthenticationResponseDto) readValue(responseJson, AuthenticationResponseDto.class);

		assertEquals(200, status);
		assertNotNull(responseDto.token());
	}

	@Test
	void naoDeveAutenticar() {
		AuthenticationDto authDto = new AuthenticationDto("user@email.com", "senha_errada");

		MvcResult result = doRequest("post", "/auth/login", authDto);
		String responseJson = getContentAsStringFromResponse(result.getResponse());
		Integer status = result.getResponse().getStatus();

		assertEquals(500, status);
		assertTrue(responseJson.contains("org.springframework.security.authentication.BadCredentialsException: Bad credentials"));
	}

}
