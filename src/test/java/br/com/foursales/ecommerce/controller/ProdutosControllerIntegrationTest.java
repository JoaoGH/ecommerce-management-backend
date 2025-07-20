package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.AbstractIntegrationTestSupport;
import br.com.foursales.ecommerce.dto.ProdutoDto;
import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProdutosControllerIntegrationTest extends AbstractIntegrationTestSupport {

	@Autowired
	private ProdutoRepository produtoRepository;

	@BeforeEach
	public void setUp() {
		produtoRepository.deleteAll();

		createUsers();

		Produto produto1 = new Produto();
		produto1.setNome("Produto 1");
		produto1.setDescricao("Produto 1 Descricao");
		produto1.setPreco(BigDecimal.valueOf(10));
		produto1.setCategoria("Produto 1 Categoria");
		produto1.setQuantidadeEmEstoque(10);

		Produto produto2 = new Produto();
		produto2.setNome("Produto 2");
		produto2.setDescricao("Produto 2 Descricao");
		produto2.setPreco(BigDecimal.valueOf(20));
		produto2.setCategoria("Produto 2 Categoria");
		produto2.setQuantidadeEmEstoque(20);

		produtoRepository.saveAll(List.of(produto1, produto2));
	}

	@Test
	void deveSalvarProdutoComSucesso() {
		String token = getAuthenticatedTokenAdmin();
		ProdutoDto novoProduto = new ProdutoDto(
				null,
				"Produto 1",
				"Descrição",
				new BigDecimal("1.99"),
				"Categoria",
				10
		);
		MvcResult saveResult = doRequest("POST", "/produto", novoProduto, token);
		assertEquals(201, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoSalvarProdutoSemAutorizacao() {
		ProdutoDto novoProduto = new ProdutoDto(
				null,
				"Produto 1",
				"Descrição",
				new BigDecimal("1.99"),
				"Categoria",
				10
		);
		MvcResult result = doRequest("POST", "/produto", novoProduto);
		assertEquals(403, result.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoSalvarProdutoSemAutorizacaoComUser() {
		String token = getAuthenticatedTokenUser();
		ProdutoDto novoProduto = new ProdutoDto(
				null,
				"Produto 1",
				"Descrição",
				new BigDecimal("1.99"),
				"Categoria",
				10
		);
		MvcResult result = doRequest("POST", "/produto", novoProduto, token);
		assertEquals(403, result.getResponse().getStatus());
	}

	@Test
	void deveBuscarProdutoUser() {
		String token = getAuthenticatedTokenUser();
		UUID id = produtoRepository.findAll().get(0).getId();
		MvcResult result = doRequest("GET", "/produto/" + id, null, token);
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void deveBuscarProdutoAdmin() {
		String token = getAuthenticatedTokenAdmin();
		UUID id = produtoRepository.findAll().get(0).getId();
		MvcResult result = doRequest("GET", "/produto/" + id, null, token);
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoBuscarProdutoSemAutorizacao() {
		UUID id = produtoRepository.findAll().get(0).getId();
		MvcResult result = doRequest("GET", "/produto/" + id, null);
		assertEquals(403, result.getResponse().getStatus());
	}

	@Test
	void deveListarProdutosComSucessoUser() {
		String token = getAuthenticatedTokenAdmin();
		MvcResult saveResult = doRequest("GET", "/produto", null, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	void deveListarProdutosComSucessoAdmin() {
		String token = getAuthenticatedTokenAdmin();
		MvcResult saveResult = doRequest("GET", "/produto", null, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoListarProdutosSemAutorizacao() {
		MvcResult result = doRequest("GET", "/produto", null);
		assertEquals(403, result.getResponse().getStatus());
	}

	@Test
	void deveAtualizarProdutoComSucesso() {
		String token = getAuthenticatedTokenAdmin();
		UUID id = produtoRepository.findAll().get(0).getId();
		ProdutoDto atualizacao = new ProdutoDto(
				id,
				"Produto Atualizado",
				"Descrição Atualizada",
				new BigDecimal("2.99"),
				"Categoria Atualizada",
				111
		);
		MvcResult updateResult = doRequest("PUT", "/produto/" + id, atualizacao, token);
		assertEquals(200, updateResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoAtualizarProdutoSemAutorizacao() {
		UUID id = produtoRepository.findAll().get(0).getId();
		ProdutoDto atualizacao = new ProdutoDto(
				id,
				"Produto Atualizado",
				"Descrição Atualizada",
				new BigDecimal("2.99"),
				"Categoria Atualizada",
				111
		);
		MvcResult updateResult = doRequest("PUT", "/produto/" + id, atualizacao);
		assertEquals(403, updateResult.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoAtualizarProdutoSemAutorizacaoComUser() {
		String token = getAuthenticatedTokenUser();
		UUID id = produtoRepository.findAll().get(0).getId();
		ProdutoDto atualizacao = new ProdutoDto(
				id,
				"Produto 1 Atualizado",
				"Descrição Atualizada",
				new BigDecimal("2.99"),
				"Categoria Atualizada",
				11
		);
		MvcResult updateResult = doRequest("PUT", "/produto/" + id, atualizacao, token);
		assertEquals(403, updateResult.getResponse().getStatus());
	}

	@Test
	void deveDeletarProdutoComSucesso() {
		String token = getAuthenticatedTokenAdmin();
		UUID id = produtoRepository.findAll().get(0).getId();

		MvcResult result = doRequest("DELETE", "/produto/" + id, null, token);
		assertEquals(204, result.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoDeletarProdutoSemAutorizacaoComUser() {
		String token = getAuthenticatedTokenUser();
		UUID id = produtoRepository.findAll().get(0).getId();

		MvcResult result = doRequest("DELETE", "/produto/" + id, null, token);
		assertEquals(403, result.getResponse().getStatus());
	}

	@Test
	void deveRetornar403AoDeletarProdutoSemAutorizacao() {
		UUID id = produtoRepository.findAll().get(0).getId();

		MvcResult result = doRequest("DELETE", "/produto/" + id, null);
		assertEquals(403, result.getResponse().getStatus());
	}


}
