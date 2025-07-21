package br.com.foursales.ecommerce.exception;

import br.com.foursales.ecommerce.entity.Pedido;
import org.springframework.http.HttpStatus;

public class EstoqueInsuficiente extends DefaultApiException {

	public final static String MESSAGE = "Quantidade de estoque insuficiente. Pedido cancelado.";

	private Pedido pedido;

	public EstoqueInsuficiente(Pedido pedido) {
		super(MESSAGE, HttpStatus.UNPROCESSABLE_ENTITY);
		this.pedido = pedido;
	}

	public Pedido getPedido() {
		return pedido;
	}
}
