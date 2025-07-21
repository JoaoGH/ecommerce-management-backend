package br.com.foursales.ecommerce.config;

import br.com.foursales.ecommerce.enums.RolesPermitidas;
import br.com.foursales.ecommerce.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement((session) -> {
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				})
				.authorizeHttpRequests(authorize -> { authorize
						.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

						.requestMatchers(HttpMethod.POST, "/produto").hasRole(RolesPermitidas.ADMIN.name())
						.requestMatchers(HttpMethod.PUT, "/produto/**").hasRole(RolesPermitidas.ADMIN.name())
						.requestMatchers(HttpMethod.DELETE, "/produto/**").hasRole(RolesPermitidas.ADMIN.name())

						.requestMatchers(HttpMethod.POST, "/role").hasRole(RolesPermitidas.ADMIN.name())
						.requestMatchers(HttpMethod.PUT, "/role/**").hasRole(RolesPermitidas.ADMIN.name())
						.requestMatchers(HttpMethod.DELETE, "/role/**").hasRole(RolesPermitidas.ADMIN.name())
						.requestMatchers(HttpMethod.GET, "/role/**").hasRole(RolesPermitidas.ADMIN.name())

						.requestMatchers(HttpMethod.POST, "/usuario").hasRole(RolesPermitidas.ADMIN.name())
						.requestMatchers(HttpMethod.PUT, "/usuario/**").hasRole(RolesPermitidas.ADMIN.name())
						.requestMatchers(HttpMethod.DELETE, "/usuario/**").hasRole(RolesPermitidas.ADMIN.name())
						.requestMatchers(HttpMethod.GET, "/usuario/**").hasRole(RolesPermitidas.ADMIN.name())

						.requestMatchers(HttpMethod.GET, "/relatorio/**").hasRole(RolesPermitidas.ADMIN.name())

						.anyRequest().authenticated();
				})
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

}
