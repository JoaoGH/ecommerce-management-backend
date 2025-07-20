package br.com.foursales.ecommerce;

import br.com.foursales.ecommerce.dto.security.AuthenticationDto;
import br.com.foursales.ecommerce.dto.security.AuthenticationResponseDto;
import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.RoleRepository;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import br.com.foursales.ecommerce.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

public abstract class AbstractIntegrationTestSupport {

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	protected UsuarioRepository usuarioRepository;
	@Autowired
	protected RoleRepository roleRepository;
	@Autowired
	protected UsuarioService usuarioService;

	protected void createUsers() {
		usuarioRepository.deleteAll();
		roleRepository.deleteAll();

		Role roleUser = new Role();
		roleUser.setNome("USER");
		Role roleAdmin = new Role();
		roleAdmin.setNome("ADMIN");
		roleRepository.saveAll(List.of(roleUser, roleAdmin));

		Usuario userUser = new Usuario();
		userUser.setNome("USER");
		userUser.setEmail("user@email.com");
		userUser.setSenha("user_password");
		userUser.setRoles(Set.of(roleUser));
		usuarioService.save(userUser);

		Usuario userAdmin = new Usuario();
		userAdmin.setNome("ADMIN");
		userAdmin.setEmail("admin@email.com");
		userAdmin.setSenha("admin_password");
		userAdmin.setRoles(Set.of(roleAdmin));
		usuarioService.save(userAdmin);
	}

	protected String getAuthenticatedTokenUser() {
		return getAuthenticatedToken("user@email.com", "user_password");
	}

	protected String getAuthenticatedTokenAdmin() {
		return getAuthenticatedToken("admin@email.com", "admin_password");
	}

	protected String getAuthenticatedToken(String user, String senha) {
		AuthenticationDto authDto = new AuthenticationDto(user, senha);

		MvcResult result = doRequest("post", "/auth/login", authDto);
		String responseJson = getContentAsStringFromResponse(result.getResponse());
		AuthenticationResponseDto responseDto = (AuthenticationResponseDto) readValue(
				responseJson, AuthenticationResponseDto.class);

		return responseDto.token();
	}

	protected Object readValue(String responseJson, Class clazz) {
		try {
			return objectMapper.readValue(responseJson, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected MvcResult doRequest(String method, String endPoint, Object content) {
		return doRequest(method, endPoint, content, null);
	}

	protected MvcResult doRequest(String method, String endPoint, Object content, String token) {
		try {
			MockHttpServletRequestBuilder requestBuilder;

			switch (method.toUpperCase()) {
				case "POST":
					requestBuilder = MockMvcRequestBuilders.post(endPoint);
					break;
				case "PUT":
					requestBuilder = MockMvcRequestBuilders.put(endPoint);
					break;
				case "DELETE":
					requestBuilder = MockMvcRequestBuilders.delete(endPoint);
					break;
				default:
					requestBuilder = MockMvcRequestBuilders.get(endPoint);
			}

			if (content != null && !method.equalsIgnoreCase("GET")) {
				requestBuilder
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(content));
			}

			if (token != null && !token.isBlank()) {
				requestBuilder.header("Authorization", "Bearer " + token);
			}

			return mockMvc.perform(requestBuilder).andReturn();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String getContentAsStringFromResponse(MockHttpServletResponse response) {
		try {
			return response.getContentAsString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



}
