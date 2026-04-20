package itk.onlineshop.service;

import itk.onlineshop.exception.NotFoundException;
import itk.onlineshop.model.Product;
import itk.onlineshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Продукт не найден: %d.", id)));
    }

    public Product create(Product p) {
        return repo.save(p);
    }

    public Product update(Long id, Product p) {
        Product existing = get(id);
        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        existing.setPrice(p.getPrice());
        existing.setQuantityInStock(p.getQuantityInStock());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException((String.format("Продукт не найден: %d.", id)));
        }
        repo.deleteById(id);
    }
}