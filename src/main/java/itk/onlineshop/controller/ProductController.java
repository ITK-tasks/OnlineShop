package itk.onlineshop.controller;

import itk.onlineshop.dto.ProductDTO;
import itk.onlineshop.exception.BadRequestException;
import itk.onlineshop.model.Product;
import itk.onlineshop.service.ProductService;
import itk.onlineshop.util.JsonUtil;
import itk.onlineshop.util.MapperUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    private final JsonUtil jsonUtil;
    private final Validator validator;
    private final MapperUtil mapperUtil;

    public ProductController(ProductService service, JsonUtil jsonUtil,
                             Validator validator, MapperUtil mapperUtil) {
        this.service = service;
        this.jsonUtil = jsonUtil;
        this.validator = validator;
        this.mapperUtil = mapperUtil;
    }

    @GetMapping
    public String getAllProducts() throws Exception {
        List<Product> products = service.getAll();
        List<ProductDTO> dtos = products.stream()
                .map(mapperUtil::toDto)
                .toList();
        return jsonUtil.toJson(dtos);
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id) throws Exception {
        Product product = service.get(id);
        return jsonUtil.toJson(mapperUtil.toDto(product));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody String json) throws Exception {
        ProductDTO dto = jsonUtil.fromJson(json, ProductDTO.class);
        validate(dto);
        Product product = mapperUtil.toEntity(dto);
        Product saved = service.create(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jsonUtil.toJson(mapperUtil.toDto(saved)));
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @RequestBody String json) throws Exception {
        ProductDTO dto = jsonUtil.fromJson(json, ProductDTO.class);
        validate(dto);
        Product product = mapperUtil.toEntity(dto);
        Product updated = service.update(id, product);
        return jsonUtil.toJson(mapperUtil.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new BadRequestException(violations.iterator().next().getMessage());
        }
    }
}