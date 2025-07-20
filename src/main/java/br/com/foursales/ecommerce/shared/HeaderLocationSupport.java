package br.com.foursales.ecommerce.shared;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public interface HeaderLocationSupport {

	default URI generateHeaderLocation(Object id) {
		return ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(id)
				.toUri();
	}

}
