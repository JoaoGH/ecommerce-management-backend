package br.com.foursales.ecommerce.controller.security;

import br.com.foursales.ecommerce.dto.security.AuthenticationDto;
import br.com.foursales.ecommerce.dto.security.AuthenticationResponseDto;
import br.com.foursales.ecommerce.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final SecurityService securityService;

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationDto dto) {
		return ResponseEntity.ok(securityService.doLogin(dto));
	}

}
