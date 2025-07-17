package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService extends DefaultCrudService<Produto, UUID> {

	private final ProdutoRepository repository;

	@Override
	protected JpaRepository<Produto, UUID> getRepository() {
		return repository;
	}
}
