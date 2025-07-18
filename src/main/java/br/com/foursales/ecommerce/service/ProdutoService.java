package br.com.foursales.ecommerce.service;

import br.com.foursales.ecommerce.entity.Produto;
import br.com.foursales.ecommerce.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService extends DefaultCrudService<Produto, UUID> {

	private final ProdutoRepository repository;

	@Override
	protected JpaRepository<Produto, UUID> getRepository() {
		return repository;
	}

	@Override
	protected void beforeSave(Produto entity) {
		beforeSaveAndUpdate(entity);
	}

	@Override
	protected void beforeUpdate(Produto entity) {
		beforeSaveAndUpdate(entity);
	}

	protected void beforeSaveAndUpdate(Produto entity) {
		if (entity.getQuantidadeEmEstoque() == null || entity.getQuantidadeEmEstoque() < 0) {
			throw new IllegalArgumentException("Valor inválido para quantidade de itens em estoque do produto.");
		}
		if (entity.getPreco() == null || entity.getPreco().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Valor inválido para preço do produto.");
		}
	}
}
