package br.com.foursales.ecommerce.service.security;

import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.exception.DefaultApiException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TokenService {

	@Value("${api.security.jwt.secret}")
	private String secret;

	@Value("${api.security.jwt.time-to-live}")
	private Integer timeToLive;

	@Value("${spring.application.name}")
	private String issuer;

	public String generateToken(Usuario usuario) {
		try {
			LocalDateTime expiresAtLocalDateTime = LocalDateTime.now().plusMinutes(timeToLive);
			Instant expiresAt = expiresAtLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();

			Algorithm algorithm = Algorithm.HMAC256(secret);

			return JWT.create()
					.withIssuer(issuer)
					.withSubject(usuario.getEmail())
					.withExpiresAt(expiresAt)
					.sign(algorithm);
		} catch (JWTCreationException e) {
			throw new DefaultApiException("Erro ao gerar token");
		}
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);

			return JWT.require(algorithm)
					.withIssuer(issuer)
					.build()
					.verify(token)
					.getSubject();

		} catch (JWTVerificationException e) {
			return "";
		}
	}

}
