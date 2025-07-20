package br.com.foursales.ecommerce.service;


import br.com.foursales.ecommerce.dto.ItemPedidoDTO;
import br.com.foursales.ecommerce.dto.PedidoDto;
import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.enums.StatusPedido;
import br.com.foursales.ecommerce.repository.*;
import br.com.foursales.ecommerce.service.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PedidoServiceIntegrationTest {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PedidoItemRepository pedidoItemRepository;

	@MockitoBean
	private SecurityService securityService;

	@BeforeEach
	public void setUp() {
		pedidoItemRepository.deleteAll();
		pedidoRepository.deleteAll();
		produtoRepository.deleteAll();
		usuarioRepository.deleteAll();
		roleRepository.deleteAll();

		Produto produto1 = new Produto();
		produto1.setNome("Produto 1");
		produto1.setDescricao("Produto 1 Descricao");
		produto1.setPreco(BigDecimal.valueOf(123.45));
		produto1.setCategoria("Produto 1 Categoria");
		produto1.setQuantidadeEmEstoque(10);

		Produto produto2 = new Produto();
		produto2.setNome("Produto 2");
		produto2.setDescricao("Produto 2 Descricao");
		produto2.setPreco(BigDecimal.valueOf(6789));
		produto2.setCategoria("Produto 2 Categoria");
		produto2.setQuantidadeEmEstoque(10);

		produtoRepository.saveAll(List.of(produto1, produto2));

		Role role1 = new Role();
		role1.setNome("USER");
		roleService.save(role1);

		Usuario usuario1 = new Usuario();
		usuario1.setNome("Usuario 1");
		usuario1.setEmail("usuario1@email.com");
		usuario1.setSenha("senha");
		usuario1.setRoles(Set.of(role1));
		usuarioService.save(usuario1);

		Mockito.when(securityService.getCurentUser()).thenReturn(usuario1);
	}

	@Test
	public void deveCriarPedidoComProdutosValidos() {
		Produto produto = produtoRepository.findAll().getFirst();

		ItemPedidoDTO dto = new ItemPedidoDTO(null, produto.getId(), 3);

		assertDoesNotThrow(() -> {
			Pedido pedido = pedidoService.createOrder(dto);
			assertEquals(pedido.getValorTotal(), BigDecimal.valueOf(370.35));
		});
	}

	@Test
	public void deveAddProdutoNoPedido() {
		List<Produto> produtos = produtoRepository.findAll();

		ItemPedidoDTO dto = new ItemPedidoDTO(null, produtos.get(0).getId(), 3);
		Pedido pedido1 = pedidoService.createOrder(dto);

		assertEquals(pedido1.getValorTotal(), BigDecimal.valueOf(370.35));

		ItemPedidoDTO dto2 = new ItemPedidoDTO(pedido1.getId(), produtos.get(1).getId(), 3);

		assertDoesNotThrow(() -> {
			Pedido pedido2 = pedidoService.createOrder(dto2);
			assertEquals(pedido2.getValorTotal(), BigDecimal.valueOf(20737.35));
		});
	}

	@Test
	public void naoDeveCriarPedidoSemIdProduto() {
		ItemPedidoDTO dto = new ItemPedidoDTO(null, null, 3);

		assertThrows(IllegalArgumentException.class, () -> {
			pedidoService.createOrder(dto);
		});
	}

	@Test
	public void naoDeveCriarPedidoSemQuantidadeProduto() {
		List<Produto> produtos = produtoRepository.findAll();
		ItemPedidoDTO dto = new ItemPedidoDTO(null, produtos.getFirst().getId(), null);

		assertThrows(IllegalArgumentException.class, () -> {
			pedidoService.createOrder(dto);
		});
	}

	@Test
	public void naoDeveAddProdutoNoPedidoComIdInvalido() {
		UUID id = UUID.randomUUID();
		while (pedidoService.get(id) != null) {
			id = UUID.randomUUID();
		}

		List<Produto> produtos = produtoRepository.findAll();
		ItemPedidoDTO dto = new ItemPedidoDTO(id, produtos.getFirst().getId(), 1);

		assertThrows(EntityNotFoundException.class, () -> {
			pedidoService.createOrder(dto);
		});
	}

	@Test
	public void naoDeveCriarPedidoComEstoqueInsuficiente() {
		List<Produto> produtos = produtoRepository.findAll();
		ItemPedidoDTO dto = new ItemPedidoDTO(null, produtos.getFirst().getId(), 100);

		assertThrows(IllegalArgumentException.class, () -> {
			pedidoService.createOrder(dto);
		});
	}

	@Test
	public void devePagarPedido() {
		Produto produto = produtoRepository.findAll().getFirst();
		ItemPedidoDTO dto = new ItemPedidoDTO(null, produto.getId(), 1);
		Pedido pedido = pedidoService.createOrder(dto);

		assertDoesNotThrow(() -> {
			PedidoDto pago = pedidoService.pay(pedido.getId());
			assertEquals(StatusPedido.PAGO, pago.status());
		});
	}

	@Test
	public void naoDevePagarPedidoIdInvalido() {
		Produto produto = produtoRepository.findAll().getFirst();
		ItemPedidoDTO dto = new ItemPedidoDTO(null, produto.getId(), 1);
		pedidoService.createOrder(dto);

		assertThrows(EntityNotFoundException.class, () -> {
			UUID id = UUID.randomUUID();
			while (pedidoService.get(id) != null) {
				id = UUID.randomUUID();
			}
			pedidoService.pay(id);
		});

		assertThrows(IllegalArgumentException.class, () -> pedidoService.pay(null));
	}

	@Test
	public void deveReduzirEstoqueAposVenda() {
		Produto produto = produtoRepository.findAll().getFirst();
		ItemPedidoDTO dto = new ItemPedidoDTO(null, produto.getId(), 1);
		Pedido pedido = pedidoService.createOrder(dto);

		Integer quantidadeEsperada = produto.getQuantidadeEmEstoque() - 1;

		assertEquals(pedido.getValorTotal(), BigDecimal.valueOf(123.45));

		PedidoDto pago = pedidoService.pay(pedido.getId());
		assertEquals(StatusPedido.PAGO, pago.status());

		Integer novaQuantidade = produtoRepository.findById(produto.getId()).get().getQuantidadeEmEstoque();
		assertEquals(quantidadeEsperada, novaQuantidade);
	}

	@Test
	public void deveCancelarPedido() {
		Produto produto = produtoRepository.findAll().getFirst();
		ItemPedidoDTO dto1 = new ItemPedidoDTO(null, produto.getId(), 1);
		Pedido pedido1 = pedidoService.createOrder(dto1);
		assertEquals(pedido1.getValorTotal(), BigDecimal.valueOf(123.45));

		ItemPedidoDTO dto2 = new ItemPedidoDTO(null, produto.getId(), 10);
		Pedido pedido2 = pedidoService.createOrder(dto2);
		assertEquals(pedido2.getValorTotal(), BigDecimal.valueOf(123450, 2));

		PedidoDto pedido2Pago = pedidoService.pay(pedido2.getId());
		assertEquals(StatusPedido.PAGO, pedido2Pago.status());

		PedidoDto pedido1Pago = pedidoService.pay(pedido1.getId());
		assertEquals(StatusPedido.CANCELADO, pedido1Pago.status());
	}

}
