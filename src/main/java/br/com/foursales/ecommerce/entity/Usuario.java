package br.com.foursales.ecommerce.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario extends AuditableEntity implements Identifiable<UUID> {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(length = 300, nullable = false)
	private String senha;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "roles_usuarios",
			joinColumns = @JoinColumn(name = "usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario usuario = (Usuario) o;
		return Objects.equals(id, usuario.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


}
