package br.com.foursales.ecommerce.exception;

import br.com.foursales.ecommerce.entity.Pedido;
import org.springframework.http.HttpStatus;

public class EstoqueInsuficiente extends DefaultApiException {

	private Pedido pedido;

	public EstoqueInsuficiente(Pedido pedido) {
		super("Quantidade de estoque insuficiente. Pedido cancelado.", HttpStatus.UNPROCESSABLE_ENTITY);
		this.pedido = pedido;
	}

	public Pedido getPedido() {
		return pedido;
	}
}
