package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.dto.ItemPedidoDTO;
import br.com.foursales.ecommerce.dto.PedidoDto;
import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.entity.PedidoItem;
import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.enums.StatusPedido;
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

	@Override
	protected JpaRepository<Pedido, UUID> getRepository() {
		return pedidoRepository;
	}

	@Transactional
	public Pedido createOrder(ItemPedidoDTO dto) {
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

		return pedido;
	}

	protected Produto getProduto(ItemPedidoDTO dto) {
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
	protected Pedido getPedido(ItemPedidoDTO itemPedidoDTO, Produto produto) {
		Pedido pedido;

		if (itemPedidoDTO.idPedido() != null) {
			pedido = get(itemPedidoDTO.idPedido());
			if (pedido == null) {
				throw new EntityNotFoundException("Entity not found with ID: " + itemPedidoDTO.idPedido());
			}
		} else {
			pedido = new Pedido();
			BigDecimal valorTotal = produto.getPreco().multiply(BigDecimal.valueOf(itemPedidoDTO.quantidade()));
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
	public PedidoDto pay(UUID idPedido) {
		Pedido pedido = validatePedidoBeforePay(idPedido);

		if (pedido.getStatus() == StatusPedido.CANCELADO) {
			PedidoDto pedidoDto = new PedidoDto(
					pedido.getId(),
					pedido.getUsuario(),
					pedido.getStatus(),
					pedido.getValorTotal()
			);
			return pedidoDto;
		}

		pedido.setStatus(StatusPedido.PAGO);
		update(pedido.getId(), pedido);

		for (PedidoItem item : pedidoItemRepository.findByPedido(pedido)) {
			Produto produto = item.getProduto();
			produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - item.getQuantidade());
			produtoService.update(produto.getId(), produto);
		}

		PedidoDto pedidoDto = new PedidoDto(
				pedido.getId(),
				pedido.getUsuario(),
				pedido.getStatus(),
				pedido.getValorTotal()
		);

		return pedidoDto;
	}

	protected Pedido validatePedidoBeforePay(UUID idPedido) {
		if (idPedido == null) {
			throw new IllegalArgumentException("Necessario informar o ID do Pedido.");
		}

		Pedido pedido = get(idPedido);
		if (pedido == null) {
			throw new EntityNotFoundException("Pedido não encontrado para o ID: " + idPedido);
		}

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

}
