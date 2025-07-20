package br.com.foursales.ecommerce.controller.report;

import br.com.foursales.ecommerce.AbstractIntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RelatorioControllerIntegrationTest extends AbstractIntegrationTestSupport {


	@BeforeEach
	public void setUp() {
		createUsers();
	}

	@Test
	public void deveListarTopCompradores() {
		String token = getAuthenticatedTokenAdmin();

		MvcResult saveResult = doRequest("GET", "/relatorio/top-compradores", null, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403ListarTopCompradoresSemAutorizacao() {
		MvcResult saveResult = doRequest("GET", "/relatorio/top-compradores", null);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403ListarTopCompradoresSemAutorizacaoComUsuer() {
		String token = getAuthenticatedTokenUser();

		MvcResult saveResult = doRequest("GET", "/relatorio/top-compradores", null, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveListarTicketMedioCompradores() {
		String token = getAuthenticatedTokenAdmin();

		MvcResult saveResult = doRequest("GET", "/relatorio/ticket-medio", null, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403ListarTicketMedioCompradoresSemAutorizacao() {
		MvcResult saveResult = doRequest("GET", "/relatorio/ticket-medio", null);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403ListarTicketMedioCompradoresSemAutorizacaoComUsuer() {
		String token = getAuthenticatedTokenUser();

		MvcResult saveResult = doRequest("GET", "/relatorio/ticket-medio", null, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveListarFaturamentoMensal() {
		String token = getAuthenticatedTokenAdmin();

		MvcResult saveResult = doRequest("GET", "/relatorio/faturamento-mensal", null, token);
		assertEquals(200, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403ListarFaturamentoMensalSemAutorizacao() {
		MvcResult saveResult = doRequest("GET", "/relatorio/faturamento-mensal", null);
		assertEquals(403, saveResult.getResponse().getStatus());
	}

	@Test
	public void deveRetornar403ListarFaturamentoMensalSemAutorizacaoComUsuer() {
		String token = getAuthenticatedTokenUser();

		MvcResult saveResult = doRequest("GET", "/relatorio/faturamento-mensal", null, token);
		assertEquals(403, saveResult.getResponse().getStatus());
	}


}
