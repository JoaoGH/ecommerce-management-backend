package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.service.DefaultCrudService;
import br.com.foursales.ecommerce.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PedidoController extends DefaultCrudController<Pedido, UUID> {

	private final PedidoService pedidoService;

	@Override
	protected DefaultCrudService<Pedido, UUID> getService() {
		return pedidoService;
	}
}
