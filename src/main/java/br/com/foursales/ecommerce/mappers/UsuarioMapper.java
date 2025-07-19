package br.com.foursales.ecommerce.mappers;

import br.com.foursales.ecommerce.dto.UsuarioDto;
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
public abstract class UsuarioMapper implements GenericMapper<Usuario, UsuarioDto> {

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
}
