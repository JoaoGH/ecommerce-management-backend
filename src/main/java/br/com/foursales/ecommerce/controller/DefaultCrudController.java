package br.com.foursales.ecommerce.controller;

import br.com.foursales.ecommerce.service.DefaultCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class DefaultCrudController<T, ID> {

	protected abstract DefaultCrudService<T, ID> getService();

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
	public ResponseEntity<T> save(@RequestBody T entity) {
		return ResponseEntity.ok(getService().save(entity));
	}

	@PutMapping("/{id}")
	public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
		return ResponseEntity.ok(getService().update(id, entity));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable ID id) {
		getService().delete(id);
		return ResponseEntity.noContent().build();
	}

}
