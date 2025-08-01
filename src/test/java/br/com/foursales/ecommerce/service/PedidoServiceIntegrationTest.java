package br.com.foursales.ecommerce.service;


import br.com.foursales.ecommerce.dto.PedidoItemDto;
import br.com.foursales.ecommerce.dto.PedidoResponseDto;
import br.com.foursales.ecommerce.entity.*;
import br.com.foursales.ecommerce.enums.StatusPedido;
import br.com.foursales.ecommerce.exception.DefaultApiException;
import br.com.foursales.ecommerce.exception.EstoqueInsuficiente;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

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

		Mockito.when(securityService.getCurrentUser()).thenReturn(usuario1);
	}

	@Test
	public void deveCriarPedidoComProdutosValidos() {
		Produto produto = produtoRepository.findAll().getFirst();

		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 3);

		assertDoesNotThrow(() -> {
			PedidoResponseDto pedido = pedidoService.createOrder(dto);
			assertEquals(pedido.valorTotal(), BigDecimal.valueOf(370.35));
		});
	}

	@Test
	public void deveAddProdutoNoPedido() {
		List<Produto> produtos = produtoRepository.findAll();

		PedidoItemDto dto = new PedidoItemDto(null, produtos.get(0).getId(), 3);
		PedidoResponseDto pedido1 = pedidoService.createOrder(dto);

		assertEquals(pedido1.valorTotal(), BigDecimal.valueOf(370.35));

		PedidoItemDto dto2 = new PedidoItemDto(pedido1.id(), produtos.get(1).getId(), 3);

		assertDoesNotThrow(() -> {
			PedidoResponseDto pedido2 = pedidoService.createOrder(dto2);
			assertEquals(pedido2.valorTotal(), BigDecimal.valueOf(20737.35));
		});
	}

	@Test
	public void naoDeveAddProdutoNoPedidoDeOutroUsuario() {
		List<Produto> produtos = produtoRepository.findAll();

		PedidoItemDto dto = new PedidoItemDto(null, produtos.get(0).getId(), 3);
		PedidoResponseDto pedido1 = pedidoService.createOrder(dto);

		assertEquals(pedido1.valorTotal(), BigDecimal.valueOf(370.35));

		PedidoItemDto dto2 = new PedidoItemDto(pedido1.id(), produtos.get(1).getId(), 3);

		trocarUsuarioAtual();
		assertThrows(DefaultApiException.class, () -> {
			pedidoService.createOrder(dto2);
		});
	}

	@Test
	public void naoDeveCriarPedidoSemIdProduto() {
		PedidoItemDto dto = new PedidoItemDto(null, null, 3);

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.createOrder(dto);
		});
	}

	@Test
	public void naoDeveCriarPedidoSemQuantidadeProduto() {
		List<Produto> produtos = produtoRepository.findAll();
		PedidoItemDto dto = new PedidoItemDto(null, produtos.getFirst().getId(), null);

		assertThrows(DefaultApiException.class, () -> {
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
		PedidoItemDto dto = new PedidoItemDto(id, produtos.getFirst().getId(), 1);

		assertThrows(EntityNotFoundException.class, () -> {
			pedidoService.createOrder(dto);
		});
	}

	@Test
	public void naoDeveAddProdutoNoPedidoPago() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);

		PedidoResponseDto pago = pedidoService.pay(pedido.id());
		assertEquals(StatusPedido.PAGO, pago.status());

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.createOrder(new PedidoItemDto(pedido.id(), produto.getId(), 1));
		});
	}

	@Test
	public void naoDeveAddProdutoNoPedidoCancelado() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);

		pedidoService.cancel(pedido.id());

		UUID idPedido = pedido.id();

		assertEquals(StatusPedido.CANCELADO, pedidoService.get(idPedido).getStatus());

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.createOrder(new PedidoItemDto(idPedido, produto.getId(), 1));
		});
	}

	@Test
	public void naoDeveCriarPedidoComEstoqueInsuficiente() {
		List<Produto> produtos = produtoRepository.findAll();
		PedidoItemDto dto = new PedidoItemDto(null, produtos.getFirst().getId(), 100);

		PedidoResponseDto responseDto = pedidoService.createOrder(dto);
		assertEquals(EstoqueInsuficiente.MESSAGE, responseDto.observacao());
	}

	@Test
	public void devePagarPedido() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);

		assertDoesNotThrow(() -> {
			PedidoResponseDto pago = pedidoService.pay(pedido.id());
			assertEquals(StatusPedido.PAGO, pago.status());
		});
	}

	@Test
	public void naoDevePagarPedidoDeOutroUsuario() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);

		trocarUsuarioAtual();

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.pay(pedido.id());
		});
	}

	@Test
	public void naoDevePagarPedidoIdInvalido() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		pedidoService.createOrder(dto);

		assertThrows(EntityNotFoundException.class, () -> {
			UUID id = UUID.randomUUID();
			while (pedidoService.get(id) != null) {
				id = UUID.randomUUID();
			}
			pedidoService.pay(id);
		});

		assertThrows(DefaultApiException.class, () -> pedidoService.pay(null));
	}

	@Test
	public void deveReduzirEstoqueAposVenda() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);

		Integer quantidadeEsperada = produto.getQuantidadeEmEstoque() - 1;

		assertEquals(pedido.valorTotal(), BigDecimal.valueOf(123.45));

		PedidoResponseDto pago = pedidoService.pay(pedido.id());
		assertEquals(StatusPedido.PAGO, pago.status());

		Integer novaQuantidade = produtoRepository.findById(produto.getId()).get().getQuantidadeEmEstoque();
		assertEquals(quantidadeEsperada, novaQuantidade);
	}

	@Test
	public void deveCancelarPedido() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto1 = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido1 = pedidoService.createOrder(dto1);
		assertEquals(pedido1.valorTotal(), BigDecimal.valueOf(123.45));

		PedidoItemDto dto2 = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido2 = pedidoService.createOrder(dto2);
		assertEquals(pedido2.valorTotal(), BigDecimal.valueOf(12345, 2));

		PedidoResponseDto pedido2Pago = pedidoService.pay(pedido2.id());
		assertEquals(StatusPedido.PAGO, pedido2Pago.status());

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.pay(pedido1.id());
		});
	}

	@Test
	public void deveCancelarPedidoPorSolicitante() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);

		pedidoService.cancel(pedido.id());

		assertEquals(StatusPedido.CANCELADO, pedidoRepository.findById(pedido.id()).get().getStatus());
	}

	@Test
	public void naoDeveCancelarPedidoPagoPorSolicitante() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);
		pedidoService.pay(pedido.id());

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.cancel(pedido.id());
		});
	}

	@Test
	public void naoDeveCancelarPedidoCanceladoPorSolicitante() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);
		pedidoService.cancel(pedido.id());

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.cancel(pedido.id());
		});
	}

	@Test
	public void naoDeveCancelarPedidoDeOutroUsuario() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);
		pedidoService.pay(pedido.id());

		trocarUsuarioAtual();

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.cancel(pedido.id());
		});
	}

	@Test
	public void naoDeveCriarMesmoProdutoDuasVezesNoMesmoPedido() {
		Produto produto = produtoRepository.findAll().getFirst();

		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 3);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);

		assertEquals(pedido.valorTotal(), BigDecimal.valueOf(370.35));

		PedidoItemDto dto2 = new PedidoItemDto(pedido.id(), produto.getId(), 3);
		pedido = pedidoService.createOrder(dto2);

		assertEquals(pedido.valorTotal(), BigDecimal.valueOf(74070, 2));

		List<PedidoItem> listaItens = pedidoItemRepository.findAll().stream().filter(item -> {
			return item.getProduto().getId().equals(produto.getId());
		}).toList();

		assertEquals(1, listaItens.size());
		assertEquals(6, listaItens.getFirst().getQuantidade());

	}

	@Test
	public void naoDevePermitirPagamentoDePedidoJaPago() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido = pedidoService.createOrder(dto);

		PedidoResponseDto pago = pedidoService.pay(pedido.id());
		assertEquals(StatusPedido.PAGO, pago.status());

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.pay(pedido.id());
		});
	}

	@Test
	public void naoDevePermitirPagamentoDePedidoCancelado() {
		Produto produto = produtoRepository.findAll().getFirst();
		PedidoItemDto dto1 = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido1 = pedidoService.createOrder(dto1);

		PedidoItemDto dto2 = new PedidoItemDto(null, produto.getId(), 1);
		PedidoResponseDto pedido2 = pedidoService.createOrder(dto2);
		PedidoResponseDto pedido2Pago = pedidoService.pay(pedido2.id());
		assertEquals(StatusPedido.PAGO, pedido2Pago.status());

		assertThrows(DefaultApiException.class, () -> {
			pedidoService.pay(pedido1.id());
		});
	}

	private void trocarUsuarioAtual() {
		Usuario usuario = new Usuario();
		usuario.setNome("Outro Usuario");
		usuario.setEmail("outro_usuario@email.com");
		usuario.setSenha("senha");
		usuario.setRoles(Set.of(roleRepository.findAll().getFirst()));
		usuarioService.save(usuario);
		Mockito.when(securityService.getCurrentUser()).thenReturn(usuario);
	}


}
