package br.com.foursales.ecommerce.service.report;

import br.com.foursales.ecommerce.dto.PedidoItemDto;
import br.com.foursales.ecommerce.dto.PedidoResponseDto;
import br.com.foursales.ecommerce.dto.report.UsuarioTicketMedioDto;
import br.com.foursales.ecommerce.dto.report.UsuarioTopCompradorDto;
import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.*;
import br.com.foursales.ecommerce.service.PedidoService;
import br.com.foursales.ecommerce.service.UsuarioService;
import br.com.foursales.ecommerce.service.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class RelatorioServiceIntegrationTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private RelatorioService relatorioService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private PedidoService pedidoService;
	@MockitoBean
	private SecurityService securityService;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PedidoItemRepository pedidoItemRepository;

	@BeforeEach
	void setUp() {
		pedidoItemRepository.deleteAll();
		pedidoRepository.deleteAll();
		produtoRepository.deleteAll();
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

		criarCenarioCompras();
	}

	@Test
	public void deveRetornarTopUsuariosCompradores() {
		Usuario usuario = usuarioRepository.findByEmail("adm@email.com").orElse(null);
		Mockito.when(securityService.getCurrentUser()).thenReturn(usuario);

		List<UsuarioTopCompradorDto> topUsuarios = relatorioService.getTopCompradores();
		assertEquals(5, topUsuarios.size());

		UsuarioTopCompradorDto top = topUsuarios.get(0);
		assertEquals("Usuario 10", top.nome());
	}

	@Test
	public void naoDeveRetornarTopUsuariosCompradoresSemAcesso() {
		assertThrows(AccessDeniedException.class, () -> {
			relatorioService.getTopCompradores();
		});
	}

	@Test
	public void deveRetornarTicketMedioUsuarios() {
		Usuario usuario = usuarioRepository.findByEmail("adm@email.com").orElse(null);
		Mockito.when(securityService.getCurrentUser()).thenReturn(usuario);

		List<UsuarioTicketMedioDto> ticketMedioList = relatorioService.getTicketMedio();
		assertEquals(10, ticketMedioList.size());

		UsuarioTicketMedioDto user10 = ticketMedioList.stream()
				.filter(ticket -> ticket.nome().equals("Usuario 10"))
				.findFirst()
				.get();
		assertEquals("Usuario 10", user10.nome());
		assertEquals(BigDecimal.valueOf(641667, 2), user10.ticketMedio());
	}

	@Test
	public void naoDeveRetornarTicketMedioUsuariosSemAcesso() {
		assertThrows(AccessDeniedException.class, () -> {
			relatorioService.getTicketMedio();
		});
	}

	private void criarCenarioCompras() {
		Role role = roleRepository.findByNome("USER").orElseThrow();
		List<Usuario> usuarios = new ArrayList<>();

		for (int i = 1; i <= 10; i++) {
			Usuario u = new Usuario();
			u.setNome("Usuario " + i);
			u.setEmail("usuario" + i + "@email.com");
			u.setSenha("senha123");
			u.setRoles(Set.of(role));
			usuarios.add(usuarioService.save(u));
		}

		List<Produto> produtos = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			Produto produto = new Produto();
			produto.setNome("Produto " + i);
			produto.setCategoria("Categoria " + i);
			produto.setDescricao("Descricao " + i);
			produto.setQuantidadeEmEstoque(i * 10);
			produto.setPreco(BigDecimal.valueOf(i * 100));
			produtos.add(produto);
		}
		produtoRepository.saveAll(produtos);

		Map<UUID, UUID> pedidosPorUsuario = new HashMap<>();
		for (int i = 0; i < usuarios.size(); i++) {
			Usuario usuario = usuarios.get(i);
			Mockito.when(securityService.getCurrentUser()).thenReturn(usuario);

			for (int j = 0; j <= i; j++) {
				Produto produtoEscolhido = produtos.get(j % produtos.size());
				int quantidade = (j + 1);

				UUID idPedido = (j % 2 == 0 && pedidosPorUsuario.containsKey(usuario.getId()))
						? pedidosPorUsuario.get(usuario.getId())
						: null;

				PedidoItemDto pedidoItemDto = new PedidoItemDto(
						idPedido,
						produtoEscolhido.getId(),
						quantidade
				);

				PedidoResponseDto pedido = pedidoService.createOrder(pedidoItemDto);
				if (idPedido == null) {
					pedidosPorUsuario.put(usuario.getId(), pedido.id());
				}
			}
		}
	}

}
