package itk.onlineshop.service;

import itk.onlineshop.model.Product;
import itk.onlineshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProduct(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Product saveProduct(Product p) {
        return repo.save(p);
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}