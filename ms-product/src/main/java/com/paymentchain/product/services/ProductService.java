package com.paymentchain.product.services;

import com.paymentchain.product.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProduct(long id);


    List<Product> getAllProducts();

    long createProduct(Product product);

    void deleteProduct(long id);

    Product editProduct(long id, Product product);
}
