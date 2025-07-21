package br.com.foursales.ecommerce.mappers;

import br.com.foursales.ecommerce.dto.RoleDto;
import br.com.foursales.ecommerce.dto.RoleResponseDto;
import br.com.foursales.ecommerce.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends GenericMapper<Role, RoleDto, RoleResponseDto> {

}
