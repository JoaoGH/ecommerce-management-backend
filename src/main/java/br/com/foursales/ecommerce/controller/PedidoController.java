package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.dto.PedidoItemDto;
import br.com.foursales.ecommerce.dto.PedidoResponseDto;
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
	public ResponseEntity<PedidoResponseDto> add(@RequestBody PedidoItemDto dto) {
		return ResponseEntity.ok(pedidoService.createOrder(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<PedidoResponseDto> pay(@PathVariable UUID id) {
		return ResponseEntity.ok(pedidoService.pay(id));
	}

	@GetMapping
	public ResponseEntity<List<PedidoResponseDto>> list() {
		return ResponseEntity.ok(pedidoService.listar());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancel(@PathVariable UUID id) {
		pedidoService.cancel(id);
		return ResponseEntity.noContent().build();
	}

}
