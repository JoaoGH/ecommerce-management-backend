package br.com.foursales.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DefaultApiException extends RuntimeException {

	private String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	private String path = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

	public DefaultApiException(String message) {
		super(message);
	}

	public DefaultApiException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public DefaultApiException(Exception exception) {
		super(exception);
	}

	public DefaultApiException(Exception exception, HttpStatus status) {
		super(exception);
		this.status = status;
	}

	public ResponseEntity<?> getResponse() {
		return ResponseEntity.status(status).body(toJSON());
	}

	protected Map toJSON() {
		Map<String, Object> json = new HashMap<>();

		json.put("message", this.getMessage());
		json.put("timestamp", this.timestamp);
		json.put("status", this.status.value());
		json.put("path", this.path);

		return json;
	}
}
