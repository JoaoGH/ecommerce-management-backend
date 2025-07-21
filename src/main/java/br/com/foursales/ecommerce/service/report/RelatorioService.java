package br.com.foursales.ecommerce.service.report;

import br.com.foursales.ecommerce.dto.report.FaturamentoMensalDto;
import br.com.foursales.ecommerce.dto.report.UsuarioTicketMedioDto;
import br.com.foursales.ecommerce.dto.report.UsuarioTopCompradorDto;
import br.com.foursales.ecommerce.repository.PedidoRepository;
import br.com.foursales.ecommerce.repository.UsuarioRepository;
import br.com.foursales.ecommerce.security.SecurityFilter;
import br.com.foursales.ecommerce.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

	private final SecurityService securityService;
	private final UsuarioRepository usuarioRepository;
	private final PedidoRepository pedidoRepository;

	public List<UsuarioTopCompradorDto> getTopCompradores() {
		if (!securityService.getCurrentUser().isAdmin()) {
			throw new AccessDeniedException("Acesso negado");
		}

		return usuarioRepository.findTop5UsuariosMaisCompraram(PageRequest.of(0, 5));
	}

	public List<UsuarioTicketMedioDto> getTicketMedio() {
		if (!securityService.getCurrentUser().isAdmin()) {
			throw new AccessDeniedException("Acesso negado");
		}

		return usuarioRepository.calcularTicketMedioPorUsuario();
	}

	public FaturamentoMensalDto calcularFaturamentoMensal() {
		if (!securityService.getCurrentUser().isAdmin()) {
			throw new AccessDeniedException("Acesso negado");
		}

		return pedidoRepository.calcularFaturamentoMensal();
	}

}
