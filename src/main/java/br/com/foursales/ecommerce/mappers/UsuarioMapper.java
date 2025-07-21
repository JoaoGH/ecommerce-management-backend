package br.com.foursales.ecommerce.mappers;

import br.com.foursales.ecommerce.dto.RoleResponseDto;
import br.com.foursales.ecommerce.dto.UsuarioDto;
import br.com.foursales.ecommerce.dto.UsuarioResponseDto;
import br.com.foursales.ecommerce.entity.Role;
import br.com.foursales.ecommerce.entity.Usuario;
import br.com.foursales.ecommerce.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class UsuarioMapper implements GenericMapper<Usuario, UsuarioDto, UsuarioResponseDto> {

	@Autowired
	RoleRepository roleRepository;

	@Override
	@Mapping(target = "roles", expression = "java(mapRoles(dto.roles()))")
	public abstract Usuario toEntity(UsuarioDto dto);

	@Override
	@Mapping(target = "roles", expression = "java(mapRoles(entity.getRoles()))")
	public abstract UsuarioDto toDto(Usuario entity);

	List<String> mapRoles(Set<Role> roles) {
		return roles.stream()
				.map(Role::getNome)
				.collect(Collectors.toList());
	}

	Set<Role> mapRoles(List<String> roles) {
		return roles.stream()
				.map(nome -> roleRepository.findByNome(nome).orElse(null))
				.collect(Collectors.toSet());
	}

	@Override
	public List<UsuarioResponseDto> toResponseList(List<Usuario> entities) {
		return entities.stream().map(this::toResponse).toList();
	}

	@Override
	public UsuarioResponseDto toResponse(Usuario entity) {
		List<RoleResponseDto> roleResponses = entity.getRoles().stream()
				.map(role -> new RoleResponseDto(role.getId(), role.getNome()))
				.toList();

		return new UsuarioResponseDto(entity.getId(), entity.getNome(), entity.getEmail(), roleResponses);
	}
}
