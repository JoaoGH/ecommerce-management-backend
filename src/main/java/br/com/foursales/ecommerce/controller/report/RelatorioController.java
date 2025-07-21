package br.com.foursales.ecommerce.controller.report;

import br.com.foursales.ecommerce.dto.report.FaturamentoMensalDto;
import br.com.foursales.ecommerce.dto.report.UsuarioTicketMedioDto;
import br.com.foursales.ecommerce.dto.report.UsuarioTopCompradorDto;
import br.com.foursales.ecommerce.service.report.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class RelatorioController {

	private final RelatorioService relatorioService;

	@GetMapping("/top-compradores")
	public ResponseEntity<List<UsuarioTopCompradorDto>> topCompradores() {
		return ResponseEntity.ok(relatorioService.getTopCompradores());
	}

	@GetMapping("/ticket-medio")
	public ResponseEntity<List<UsuarioTicketMedioDto>> ticketMedio() {
		return ResponseEntity.ok(relatorioService.getTicketMedio());
	}

	@GetMapping("/faturamento-mensal")
	public FaturamentoMensalDto getFaturamentoMensal() {
		return relatorioService.calcularFaturamentoMensal();
	}

}
