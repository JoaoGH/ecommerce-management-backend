package br.com.foursales.ecommerce.mappers;

import br.com.foursales.ecommerce.dto.*;
import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.entity.PedidoItem;
import br.com.foursales.ecommerce.repository.PedidoItemRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, ProdutoMapper.class})
public abstract class PedidoMapper implements GenericMapper<Pedido, PedidoDto, PedidoResponseDto> {

	@Autowired
	protected PedidoItemRepository pedidoItemRepository;

	@Override
	@Mapping(target = "itens", expression = "java(fillItens(entity))")
	public abstract PedidoResponseDto toResponse(Pedido entity);

	@Override
	public List<PedidoResponseDto> toResponseList(List<Pedido> entities) {
		return entities.stream().map(this::toResponse).toList();
	}

	List<PedidoItemResponseDto> fillItens(Pedido pedido) {
		List<PedidoItem> itens = pedidoItemRepository.findByPedido(pedido);

		List<PedidoItemResponseDto> itensDto = itens.stream()
				.map(item -> new PedidoItemResponseDto(
						new ProdutoResponseDto(
								item.getProduto().getId(),
								item.getProduto().getNome(),
								item.getProduto().getDescricao(),
								item.getProduto().getPreco(),
								item.getProduto().getCategoria(),
								item.getProduto().getQuantidadeEmEstoque()
						),
						item.getQuantidade(),
						item.getPrecoUnitario()
				)).toList();

		return itensDto;
	}

}
