package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.mappers.GenericMapper;
import br.com.foursales.ecommerce.service.DefaultCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class DefaultCrudController<T, DTO, ID> {

	protected abstract DefaultCrudService<T, ID> getService();

	protected abstract GenericMapper<T, DTO> getMapper();

	@GetMapping
	public ResponseEntity<List<T>> list() {
		return ResponseEntity.ok(getService().list());
	}

	@GetMapping("/{id}")
	public ResponseEntity<T> get(@PathVariable ID id) {
		T entity = getService().get(id);
		return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<T> save(@RequestBody DTO dto) {
		T entity = getMapper().toEntity(dto);
		return ResponseEntity.ok(getService().save(entity));
	}

	@PutMapping("/{id}")
	public ResponseEntity<T> update(@PathVariable ID id, @RequestBody DTO dto) {
		return ResponseEntity.ok(getService().update(id, getMapper().toEntity(dto)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable ID id) {
		getService().delete(id);
		return ResponseEntity.noContent().build();
	}

}
