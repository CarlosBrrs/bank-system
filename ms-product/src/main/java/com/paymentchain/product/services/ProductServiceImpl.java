package com.paymentchain.product.services;

import com.paymentchain.product.entities.Product;
import com.paymentchain.product.repositories.ProductRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> getProduct(long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public long createProduct(Product product) {
        return productRepository.save(product).getId();
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> customerOptional = productRepository.findById(id);
        customerOptional.ifPresent(productRepository::delete);
    }

    @Override
    public Product editProduct(long id, Product product) {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()) {
            Product dbProduct = productOptional.get();
            dbProduct.setCode(product.getCode());
            dbProduct.setName(product.getName());
            return productRepository.save(dbProduct);
        }
        return product;
    }
}
