package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.dto.UsuarioDto;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.mappers.GenericMapper;
import br.com.foursales.ecommerce.mappers.UsuarioMapper;
import br.com.foursales.ecommerce.service.DefaultCrudService;
import br.com.foursales.ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/usuario")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class UsuarioController extends DefaultCrudController<Usuario, UsuarioDto, UUID> {

	private final UsuarioService service;
	private final UsuarioMapper mapper;

	@Override
	protected DefaultCrudService<Usuario, UUID> getService() {
		return service;
	}

	@Override
	protected GenericMapper<Usuario, UsuarioDto> getMapper() {
		return mapper;
	}
}

