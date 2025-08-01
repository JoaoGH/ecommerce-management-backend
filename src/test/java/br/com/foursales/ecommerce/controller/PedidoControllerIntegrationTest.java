package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.AbstractIntegrationTestSupport;
import br.com.foursales.ecommerce.dto.PedidoItemDto;
import br.com.foursales.ecommerce.dto.PedidoResponseDto;
import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PedidoControllerIntegrationTest extends AbstractIntegrationTestSupport {

	@Autowired
	private PedidoItemRepository pedidoItemRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private ProdutoRepository produtoRepository;

	@BeforeEach
	public void setUp() {
		pedidoItemRepository.deleteAll();
		pedidoRepository.deleteAll();
		produtoRepository.deleteAll();

		createUsers();

		Produto produto1 = new Produto();
		produto1.setNome("Produto 1");
		produto1.setDescricao("Produto 1 Descricao");
		produto1.setPreco(BigDecimal.valueOf(10));
		produto1.setCategoria("Produto 1 Categoria");
		produto1.setQuantidadeEmEstoque(10);
		produtoRepository.save(produto1);
	}

	@Test
	void deveCriarPedidoComSucesso() {
		String token = getAuthenticatedTokenUser();

		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);

		MvcResult result = doRequest("post", "/pedido", dto, token);
		String responseJson = getContentAsStringFromResponse(result.getResponse());
		Integer status = result.getResponse().getStatus();

		PedidoResponseDto responseDto = (PedidoResponseDto) readValue(responseJson, PedidoResponseDto.class);

		assertEquals(200, status);
		assertNotNull(responseDto.id());
	}

	@Test
	void deveRetornar403AoCriarPedidoSemAutenticacao() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);

		MvcResult result = doRequest("post", "/pedido", dto);
		Integer status = result.getResponse().getStatus();

		assertEquals(403, status);
	}

	@Test
	void devePagarPedidoComSucesso() {
		Produto produto = produtoRepository.findAll().getFirst();
		String token = getAuthenticatedTokenUser();
		UUID idPedido = createPedido(produto, 1, token);

		MvcResult result = doRequest("put", "/pedido/" + idPedido, null, token);
		Integer status = result.getResponse().getStatus();

		assertEquals(200, status);
	}

	@Test
	void deveRetornar403AoPagarPedidoSemAutenticacao() {
		Produto produto = produtoRepository.findAll().getFirst();
		String token = getAuthenticatedTokenUser();
		UUID idPedido = createPedido(produto, 1, token);

		MvcResult result = doRequest("put", "/pedido/" + idPedido, null);
		Integer status = result.getResponse().getStatus();

		assertEquals(403, status);
	}

	@Test
	void deveListarPedidosComSucesso() {
		String token = getAuthenticatedTokenUser();
		Produto produto = produtoRepository.findAll().getFirst();
		createPedido(produto, 1, token);
		createPedido(produto, 3, token);

		MvcResult result = doRequest("list", "/pedido", null, token);
		Integer status = result.getResponse().getStatus();

		assertEquals(200, status);
	}

	@Test
	void deveRetornar403AoListarPedidosSemAutenticacao() {
		String token = getAuthenticatedTokenUser();
		Produto produto = produtoRepository.findAll().getFirst();
		createPedido(produto, 1, token);

		MvcResult result = doRequest("list", "/pedido", null);
		Integer status = result.getResponse().getStatus();

		assertEquals(403, status);
	}

	@Test
	void deveCancelarPedidoComSucesso() {
		Produto produto = produtoRepository.findAll().getFirst();
		String token = getAuthenticatedTokenUser();
		UUID idPedido = createPedido(produto, 1, token);

		MvcResult result = doRequest("DELETE", "/pedido/" + idPedido, null, token);
		Integer status = result.getResponse().getStatus();

		assertEquals(204, status);
	}

	@Test
	void deveRetornar403AoCancelarPedidosSemAutenticacao() {
		Produto produto = produtoRepository.findAll().getFirst();
		String token = getAuthenticatedTokenUser();
		UUID idPedido = createPedido(produto, 1, token);

		MvcResult result = doRequest("DELETE", "/pedido/" + idPedido, null, null);
		Integer status = result.getResponse().getStatus();

		assertEquals(403, status);
	}

	@Test
	void naoDeveCancelarPedidoAlheio() {
		Produto produto = produtoRepository.findAll().getFirst();
		String token = getAuthenticatedTokenUser();
		UUID idPedido = createPedido(produto, 1, token);

		String token2 = getAuthenticatedTokenAdmin();

		MvcResult result = doRequest("DELETE", "/pedido/" + idPedido, null, token2);
		Integer status = result.getResponse().getStatus();

		assertEquals(500, status);
	}

	private UUID createPedido(Produto produto, Integer quantidade, String token) {
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), quantidade);
		MvcResult result = doRequest("post", "/pedido", dto, token);
		String responseJson = getContentAsStringFromResponse(result.getResponse());
		PedidoResponseDto responseDto = (PedidoResponseDto) readValue(responseJson, PedidoResponseDto.class);

		return responseDto.id();
	}

}
