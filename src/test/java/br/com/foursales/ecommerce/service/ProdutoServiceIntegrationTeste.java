package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProdutoServiceIntegrationTeste {

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@BeforeEach
	public void setUp() {
		produtoRepository.deleteAll();
	}

	@Test
	public void deveSalvarProdutoComDadosValidos() {
		Produto produto = new Produto();
		produto.setNome("Novo Produto");
		produto.setDescricao("Novo Produto Descrição");
		produto.setPreco(BigDecimal.valueOf(123.32));
		produto.setCategoria("Novo Produto Categoria");
		produto.setQuantidadeEmEstoque(3);

		assertDoesNotThrow(() -> produtoService.save(produto));
	}

	@Test
	public void naoDeveSalvarProdutoSemInformacoes() {
		Produto produto = new Produto();

		assertThrows(IllegalArgumentException.class, () -> {
			produtoService.save(produto);
		});
	}

	@Test
	public void naoDeveSalvarProdutoComPrecoNegativo() {
		Produto produto = new Produto();
		produto.setNome("Novo Produto");
		produto.setDescricao("Novo Produto Descrição");
		produto.setPreco(BigDecimal.valueOf(-123.32));
		produto.setCategoria("Novo Produto Categoria");
		produto.setQuantidadeEmEstoque(1);

		assertThrows(IllegalArgumentException.class, () -> {
			produtoService.save(produto);
		});
	}

	@Test
	public void naoDeveSalvarProdutoComEstoqueNegativo() {
		Produto produto = new Produto();
		produto.setNome("Novo Produto");
		produto.setDescricao("Novo Produto Descrição");
		produto.setPreco(BigDecimal.valueOf(123.32));
		produto.setCategoria("Novo Produto Categoria");
		produto.setQuantidadeEmEstoque(-1);

		assertThrows(IllegalArgumentException.class, () -> {
			produtoService.save(produto);
		});
	}

	@Test
	public void deveAtualizarInformacoesDoProduto() {
		Produto atualizar = createAndSaveProdutoParaAtualizar();
		atualizar.setNome("Atualizar Produto");

		assertDoesNotThrow(() -> produtoService.update(atualizar.getId(), atualizar));
	}

	@Test
	public void naoDeveAtualizarProdutoInexistente() {
		UUID id = UUID.randomUUID();
		while (produtoService.get(id) != null) {
			id = UUID.randomUUID();
		}

		UUID finalId = id;
		assertThrows(EntityNotFoundException.class, () -> {
			produtoService.update(finalId, new Produto());
		});

		assertThrows(IllegalArgumentException.class, () -> {
			produtoService.update(null, new Produto());
		});
	}

	@Test
	public void naoDeveAtualizarProdutoComPrecoNegativo() {
		Produto atualizar = createAndSaveProdutoParaAtualizar();
		atualizar.setPreco(BigDecimal.valueOf(-1));

		assertThrows(IllegalArgumentException.class, () -> {
			produtoService.update(atualizar.getId(), atualizar);
		});
	}

	@Test
	public void deveListarTodosOsProdutos() {
		Produto produto1 = new Produto();
		produto1.setNome("Produto 1");
		produto1.setDescricao("Produto 1 Descrição");
		produto1.setPreco(BigDecimal.valueOf(132));
		produto1.setCategoria("Produto 1 Categoria");
		produto1.setQuantidadeEmEstoque(1);

		Produto produto2 = new Produto();
		produto2.setNome("Produto 2");
		produto2.setDescricao("Produto 2 Descrição");
		produto2.setPreco(BigDecimal.valueOf(123.32));
		produto2.setCategoria("Produto 2 Categoria");
		produto2.setQuantidadeEmEstoque(1);

		produtoRepository.saveAll(List.of(produto1, produto2));

		assertEquals(2, produtoService.list().size());
	}

	@Test
	public void deveBuscarProdutoPorIdValido() {
		Produto produto1 = new Produto();
		produto1.setNome("Produto 1");
		produto1.setDescricao("Produto 1 Descrição");
		produto1.setPreco(BigDecimal.valueOf(132));
		produto1.setCategoria("Produto 1 Categoria");
		produto1.setQuantidadeEmEstoque(1);

		Produto produto2 = new Produto();
		produto2.setNome("Produto 2");
		produto2.setDescricao("Produto 2 Descrição");
		produto2.setPreco(BigDecimal.valueOf(123.32));
		produto2.setCategoria("Produto 2 Categoria");
		produto2.setQuantidadeEmEstoque(1);

		produtoRepository.saveAll(List.of(produto1, produto2));

		assertNotNull(produtoService.get(produto1.getId()));
	}

	@Test
	public void naoDeveRetornarProdutoComIdInvalido() {
		UUID id = UUID.randomUUID();
		while (produtoService.get(id) != null) {
			id = UUID.randomUUID();
		}

		UUID finalId = id;
		assertNull(produtoService.get(finalId));
	}

	@Test
	public void deveExcluirProdutoPorId() {
		Produto produto = new Produto();
		produto.setNome("Produto");
		produto.setDescricao("Produto Descrição");
		produto.setPreco(BigDecimal.valueOf(1));
		produto.setCategoria("Produto Categoria");
		produto.setQuantidadeEmEstoque(1);

		produtoService.save(produto);

		assertDoesNotThrow(() -> produtoService.delete(produto.getId()));
	}

	@Test
	public void naoDeveExcluirProdutoInexistente() {
		UUID id = UUID.randomUUID();
		while (produtoService.get(id) != null) {
			id = UUID.randomUUID();
		}

		UUID finalId = id;
		assertThrows(EntityNotFoundException.class, () -> {
			produtoService.delete(finalId);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			produtoService.delete(null);
		});
	}

	protected Produto createAndSaveProdutoParaAtualizar() {
		Produto produto = new Produto();
		produto.setNome("Novo Produto");
		produto.setDescricao("Novo Produto Descrição");
		produto.setPreco(BigDecimal.valueOf(123.32));
		produto.setCategoria("Novo Produto Categoria");
		produto.setQuantidadeEmEstoque(1);

		produtoService.save(produto);

		return produto;
	}

}
