package itk.onlineshop.service;

import itk.onlineshop.dto.PageResponse;
import itk.onlineshop.dto.ProductDTO;
import itk.onlineshop.exception.NotFoundException;
import itk.onlineshop.model.Product;
import itk.onlineshop.repository.ProductRepository;
import itk.onlineshop.util.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repo;
    private final MapperUtil mapperUtil;


    public ProductService(ProductRepository repo, MapperUtil mapperUtil) {
        this.repo = repo;
        this.mapperUtil = mapperUtil;
    }

    public PageResponse<ProductDTO> getAll(int page, int size) {
        Page<Product> productPage = repo.findAll(PageRequest.of(page, size));
        List<ProductDTO> dtos = productPage.getContent().stream()
                .map(mapperUtil::toDto)
                .toList();
        return new PageResponse<>(
                dtos,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
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