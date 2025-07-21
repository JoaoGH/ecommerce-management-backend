package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.dto.*;
import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.entity.PedidoItem;
import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.enums.StatusPedido;
import br.com.foursales.ecommerce.mappers.PedidoMapper;
import br.com.foursales.ecommerce.repository.PedidoItemRepository;
import br.com.foursales.ecommerce.repository.PedidoRepository;
import br.com.foursales.ecommerce.service.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService extends DefaultCrudService<Pedido, UUID> {

	private final PedidoRepository pedidoRepository;
	private final ProdutoService produtoService;
	private final PedidoItemRepository pedidoItemRepository;
	private final SecurityService securityService;
	private final PedidoMapper pedidoMapper;

	@Override
	protected JpaRepository<Pedido, UUID> getRepository() {
		return pedidoRepository;
	}

	@Transactional
	public PedidoResponseDto createOrder(PedidoItemDto dto) {
		Produto produto = getProduto(dto);
		Pedido pedido = getPedido(dto, produto);

		PedidoItem pedidoItem = pedidoItemRepository.findByPedidoAndProduto(pedido, produto);

		if (pedidoItem == null) {
			pedidoItem = new PedidoItem();
			pedidoItem.setPedido(pedido);
			pedidoItem.setProduto(produto);
			pedidoItem.setQuantidade(dto.quantidade());
		} else {
			pedidoItem.setQuantidade(pedidoItem.getQuantidade() + dto.quantidade());
		}

		pedidoItem.setPrecoUnitario(produto.getPreco());

		pedidoItemRepository.save(pedidoItem);

		update(pedido.getId(), pedido);

		return pedidoMapper.toResponse(pedido);
	}

	protected Produto getProduto(PedidoItemDto dto) {
		if (dto.idProduto() == null) {
			throw new IllegalArgumentException("Necessario informar o ID do produto.");
		}
		if (dto.quantidade() == null || dto.quantidade() < 1) {
			throw new IllegalArgumentException("Necessario informar uma quantidade valida para o produto.");
		}

		Produto produto = produtoService.get(dto.idProduto());
		if (produto == null) {
			throw new EntityNotFoundException("Produto não encontrado com o ID: " + dto.idProduto());
		}

		if (dto.quantidade() > produto.getQuantidadeEmEstoque()) {
			throw new IllegalArgumentException("Quantidade de estoque insuficiente");
		}

		return produto;
	}

	@Transactional
	protected Pedido getPedido(PedidoItemDto pedidoItemDto, Produto produto) {
		Pedido pedido;

		if (pedidoItemDto.idPedido() != null) {
			pedido = get(pedidoItemDto.idPedido());
			if (pedido == null) {
				throw new EntityNotFoundException("Entity not found with ID: " + pedidoItemDto.idPedido());
			}
			checkCanManipulatePedido(pedido);
		} else {
			pedido = new Pedido();
			BigDecimal valorTotal = produto.getPreco().multiply(BigDecimal.valueOf(pedidoItemDto.quantidade()));
			pedido.setValorTotal(valorTotal);
			save(pedido);
		}

		return pedido;
	}

	@Override
	protected void beforeSave(Pedido entity) {
		entity.setUsuario(securityService.getCurrentUser());
		entity.setStatus(StatusPedido.PENDENTE);
	}

	@Override
	protected void beforeUpdate(Pedido entity) {
		entity.setValorTotal(calculaValorTotalAtual(entity));
	}

	protected BigDecimal calculaValorTotalAtual(Pedido pedido) {
		BigDecimal total = BigDecimal.ZERO;

		for (PedidoItem item : pedidoItemRepository.findByPedido(pedido)) {
			BigDecimal subtotal = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
			total = total.add(subtotal);
		}

		return total;
	}

	@Transactional
	public PedidoResponseDto pay(UUID idPedido) {
		Pedido pedido = validatePedidoBeforePay(idPedido);

		List<PedidoItem> pedidoItems = pedidoItemRepository.findByPedido(pedido);

		if (pedido.getStatus() == StatusPedido.CANCELADO) {
			return pedidoMapper.toResponse(pedido);
		}

		pedido.setStatus(StatusPedido.PAGO);
		update(pedido.getId(), pedido);

		for (PedidoItem item : pedidoItems) {
			Produto produto = item.getProduto();
			produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - item.getQuantidade());
			produtoService.update(produto.getId(), produto);
		}

		return pedidoMapper.toResponse(pedido);
	}

	protected Pedido validatePedidoBeforePay(UUID idPedido) {
		Pedido pedido = getEntity(idPedido);

		checkCanManipulatePedido(pedido);

		for (PedidoItem item : pedidoItemRepository.findByPedido(pedido)) {
			if (item.getQuantidade() > item.getProduto().getQuantidadeEmEstoque()) {
				pedido.setStatus(StatusPedido.CANCELADO);
				return update(pedido.getId(), pedido);
			}
		}

		return pedido;
	}

	@Override
	public List<Pedido> list() {
		return pedidoRepository.findAllByUsuario(securityService.getCurrentUser());
	}

	public List<PedidoResponseDto> listar() {
		return pedidoMapper.toResponseList(list());
	}

	private Pedido getEntity(UUID uuid) {
		if (uuid == null) {
			throw new IllegalArgumentException("Necessario informar o ID do Pedido.");
		}

		Pedido pedido = get(uuid);
		if (pedido == null) {
			throw new EntityNotFoundException("Pedido não encontrado para o ID: " + uuid);
		}

		return pedido;
	}

	public void checkCanManipulatePedido(Pedido pedido) {
		if (!pedido.getUsuario().equals(securityService.getCurrentUser())) {
			throw new IllegalArgumentException("Não pode manipular o pedido de outro usuário.");
		}

		if (pedido.getStatus() == StatusPedido.PAGO) {
			throw new IllegalArgumentException("O pagamento do pedido já foi realizado.");
		}

		if (pedido.getStatus() == StatusPedido.CANCELADO) {
			throw new IllegalArgumentException("O pedido já foi cancelado.");
		}
	}

	public void cancel(UUID idPedido) {
		Pedido pedido = getEntity(idPedido);

		checkCanManipulatePedido(pedido);

		pedido.setStatus(StatusPedido.CANCELADO);
		update(pedido.getId(), pedido);
	}

}
