package microservice.demoforcicd.service;

import microservice.demoforcicd.dto.ProductRequest;
import microservice.demoforcicd.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts(String keyword);

    Product getProductById(Long id);

    Product createProduct(ProductRequest request);

    Product updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}
