package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.dto.ItemPedidoDTO;
import br.com.foursales.ecommerce.dto.PedidoDto;
import br.com.foursales.ecommerce.entity.Pedido;
import br.com.foursales.ecommerce.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoController {

	private final PedidoService pedidoService;

	@PostMapping
	public ResponseEntity<Pedido> add(@RequestBody ItemPedidoDTO dto) {
		return ResponseEntity.ok(pedidoService.createOrder(dto));
	}

	@PutMapping
	public ResponseEntity<PedidoDto> pay(@RequestBody UUID idPedido) {
		return ResponseEntity.ok(pedidoService.pay(idPedido));
	}

	@GetMapping
	public ResponseEntity<List<Pedido>> list() {
		return ResponseEntity.ok(pedidoService.list());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancel(@PathVariable UUID id) {
		pedidoService.cancel(id);
		return ResponseEntity.noContent().build();
	}

}
