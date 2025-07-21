package br.com.foursales.ecommerce.handler;

import br.com.foursales.ecommerce.exception.DefaultApiException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
		DefaultApiException retorno = new DefaultApiException(e, HttpStatus.NOT_FOUND);
		log.error("erro: ", e);
		return retorno.getResponse();
	}

	@ExceptionHandler
	public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
		DefaultApiException retorno = new DefaultApiException(e, HttpStatus.NOT_FOUND);
		log.error("erro: ", e);
		return retorno.getResponse();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
		DefaultApiException retorno = new DefaultApiException(e, HttpStatus.FORBIDDEN);
		log.error("erro: ", e);
		return retorno.getResponse();
	}

	@ExceptionHandler
	public ResponseEntity<?> handleException(Exception e) {
		DefaultApiException retorno = new DefaultApiException(e);
		log.error("erro: ", e);
		return retorno.getResponse();
	}

}
