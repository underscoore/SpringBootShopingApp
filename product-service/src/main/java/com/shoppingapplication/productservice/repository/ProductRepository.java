package com.shoppingapplication.productservice.repository;

import com.shoppingapplication.productservice.dto.ProductResponse;
import com.shoppingapplication.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    ProductResponse findAllById(Integer productId);
}
