package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.dto.ProdutoDto;
import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.mappers.GenericMapper;
import br.com.foursales.ecommerce.mappers.ProdutosMapper;
import br.com.foursales.ecommerce.service.DefaultCrudService;
import br.com.foursales.ecommerce.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/produto")
@RequiredArgsConstructor
public class ProdutosController extends DefaultCrudController<Produto, ProdutoDto, UUID> {

	protected final ProdutoService service;
	protected final ProdutosMapper mapper;

	@Override
	protected DefaultCrudService<Produto, UUID> getService() {
		return service;
	}

	@Override
	protected GenericMapper<Produto, ProdutoDto> getMapper() {
		return mapper;
	}
}
