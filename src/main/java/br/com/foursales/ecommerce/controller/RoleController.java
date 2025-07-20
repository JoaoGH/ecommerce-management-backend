package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.dto.RoleDto;
import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.mappers.GenericMapper;
import br.com.foursales.ecommerce.mappers.RoleMapper;
import br.com.foursales.ecommerce.service.DefaultCrudService;
import br.com.foursales.ecommerce.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/role")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class RoleController extends DefaultCrudController<Role, RoleDto, UUID> {

	private final RoleService service;
	private final RoleMapper mapper;

	@Override
	protected DefaultCrudService<Role, UUID> getService() {
		return service;
	}

	@Override
	protected GenericMapper<Role, RoleDto> getMapper() {
		return mapper;
	}
}
