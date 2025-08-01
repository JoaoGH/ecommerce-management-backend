package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.entity.Identifiable;
import br.com.foursales.ecommerce.mappers.GenericMapper;
import br.com.foursales.ecommerce.service.DefaultCrudService;
import br.com.foursales.ecommerce.shared.HeaderLocationSupport;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

public abstract class DefaultCrudController<
		T extends Identifiable<ID>, DTO, DTOResponse, ID>
		implements HeaderLocationSupport {

	protected abstract DefaultCrudService<T, ID> getService();

	protected abstract GenericMapper<T, DTO, DTOResponse> getMapper();

	@GetMapping
	public ResponseEntity<List<DTOResponse>> list() {
		return ResponseEntity.ok(getMapper().toResponseList(getService().list()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DTOResponse> get(@PathVariable ID id) {
		T entity = getService().get(id);
		return entity != null
				? ResponseEntity.ok(getMapper().toResponse(entity))
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid DTO dto) {
		T entity = getService().save(getMapper().toEntity(dto));
		URI location = generateHeaderLocation(entity.getId());
		return ResponseEntity.created(location).body(getMapper().toResponse(entity));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DTOResponse> update(@PathVariable ID id, @RequestBody @Valid DTO dto) {
		T entity = getService().update(id, getMapper().toEntity(dto));
		return ResponseEntity.ok(getMapper().toResponse(entity));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable ID id) {
		getService().delete(id);
		return ResponseEntity.noContent().build();
	}

}
