package br.com.foursales.ecommerce.mappers;

public interface GenericMapper<T, DTO> {
	T toEntity(DTO dto);
	DTO toDto(T entity);
}
