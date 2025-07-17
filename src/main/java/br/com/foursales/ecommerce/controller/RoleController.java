package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.service.DefaultCrudService;
import br.com.foursales.ecommerce.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController extends DefaultCrudController<Role, UUID> {

	private final RoleService service;

	@Override
	protected DefaultCrudService<Role, UUID> getService() {
		return service;
	}
}
