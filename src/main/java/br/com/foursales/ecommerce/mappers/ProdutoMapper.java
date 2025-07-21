package br.com.foursales.ecommerce.mappers;

import br.com.foursales.ecommerce.dto.ProdutoDto;
import br.com.foursales.ecommerce.dto.ProdutoResponseDto;
import br.com.foursales.ecommerce.entity.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper extends GenericMapper<Produto, ProdutoDto, ProdutoResponseDto> {

}
