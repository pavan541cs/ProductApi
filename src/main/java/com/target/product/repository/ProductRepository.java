package com.target.product.repository;

import com.target.product.model.ProductDescription;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductDescription, String> {
}
