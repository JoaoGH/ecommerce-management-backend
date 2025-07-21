package br.com.foursales.ecommerce.mappers;

import java.util.List;

public interface GenericMapper<T, DTO, R> {
	T toEntity(DTO dto);
	DTO toDto(T entity);
	R toResponse (T entity);
	List<R> toResponseList(List<T> entities);
}
