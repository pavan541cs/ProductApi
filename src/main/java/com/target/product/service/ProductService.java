package com.target.product.service;

import com.target.product.model.ProductDescription;
import com.target.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDescription createProduct(ProductDescription product) {
        log.debug("Creating product with product id ${}", product.getProduct_id());
        return productRepository.save(product);
    }

    public Optional<ProductDescription> findById(String id) {
        log.debug("Getting product with product id ${}",id);
        return productRepository.findById(id);
    }

    public List<ProductDescription> findAll() {
        log.debug("Getting all products");
        return productRepository.findAll();
    }

    public boolean updateById(String id, ProductDescription product) throws Exception {
        boolean updated = false;
        try {
            ProductDescription oldProduct = findById(product.getProduct_id()).get();
            if(product.getProduct_id().equals(id) && oldProduct.getProduct_id().equals(id)) {
                log.debug("Updating product with product id ${}", product.getProduct_id());
                productRepository.save(product);
                updated = true;
            }
            return updated;
        } catch (NoSuchElementException ex) {
            log.debug("description for product id ${} does not exist",id);
            return false;
        } catch (Exception ex) {
            log.error("Updating description for product id ${} failing with exception ",id, ex);
            throw new RuntimeException("Failed with Exception", ex);
        }
    }

    public boolean deleteById(String id) {
        try {
            ProductDescription product = findById(id).get();
            log.debug("Deleting product with product id ${}", product.getProduct_id());
            productRepository.delete(product);
            return true;
        } catch (Exception ex) {
            log.error("Deleting product with product id ${} with exception",id,ex);
            return false;
        }
    }

}
