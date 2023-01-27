package com.templatetasks.java.micronaut.oms.service.impl;

import com.templatetasks.java.micronaut.oms.data.Product;
import com.templatetasks.java.micronaut.oms.data.access.ProductRepository;
import com.templatetasks.java.micronaut.oms.data.entity.ProductEntity;
import com.templatetasks.java.micronaut.oms.data.mapper.ProductMapper;
import com.templatetasks.java.micronaut.oms.service.ProductService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 13:21
 */
@Primary
@Singleton
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final ProductMapper productMapper;

    @Inject
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.repository = productRepository;
        this.productMapper = productMapper;
    }

    @Nullable
    @Override
    public Product get(Long id) {
        return repository.findById(id)
                         .map(productMapper::map)
                         .orElse(null);
    }

    @Override
    public Product create(Product product) {
        var entity = productMapper.map(product);
        entity = repository.save(entity);
        return productMapper.map(entity);
    }

    @Override
    public Product update(Long productId, Product provided) {
        return repository.findById(productId)
                         .map(stored -> doUpdate(stored, provided))
                         .map(repository::save)
                         .map(productMapper::map)
                         .orElse(null);
    }

    private ProductEntity doUpdate(ProductEntity stored, Product provided) {
        stored.setName(provided.getName());
        stored.setDescription(provided.getDescription());
        stored.setPrice(provided.getPrice());
        return stored;
    }

    @Override
    public void delete(Long productId) {
        repository.deleteById(productId);
    }
}
