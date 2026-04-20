package itk.onlineshop.controller;

import itk.onlineshop.dto.ProductDTO;
import itk.onlineshop.model.Product;
import itk.onlineshop.service.ProductService;
import itk.onlineshop.util.JsonUtil;
import itk.onlineshop.util.MapperUtil;
import jakarta.validation.Validator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private JsonUtil jsonUtil;

    @Mock
    private Validator validator;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private ProductController productController;

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Тест");
        ProductDTO dto = new ProductDTO();
        dto.setProductId(1L);
        dto.setName("Тест");
        when(productService.getAll()).thenReturn(List.of(product));
        when(mapperUtil.toDto(any(Product.class))).thenReturn(dto);
        when(jsonUtil.toJson(anyList())).thenReturn("[{\"productId\":1,\"name\":\"Тест\"}]");
        String result = productController.getAllProducts();
        assertNotNull(result);
        verify(productService, times(1)).getAll();
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        Long id = 1L;
        Product product = new Product();
        ProductDTO dto = new ProductDTO();
        when(productService.get(id)).thenReturn(product);
        when(mapperUtil.toDto(product)).thenReturn(dto);
        when(jsonUtil.toJson(dto)).thenReturn("{\"productId\":1}");
        String result = productController.getProduct(id);
        assertNotNull(result);
        verify(productService, times(1)).get(id);
    }

    @Test
    void createProduct_ShouldReturnCreated() throws Exception {
        String json = "{\"name\":\"Новый продукт\",\"price\":100,\"quantityInStock\":5}";
        ProductDTO dto = new ProductDTO();
        Product product = new Product();
        Product savedProduct = new Product();
        when(jsonUtil.fromJson(json, ProductDTO.class)).thenReturn(dto);
        when(validator.validate(dto)).thenReturn(new HashSet<>());
        when(mapperUtil.toEntity(dto)).thenReturn(product);
        when(productService.create(product)).thenReturn(savedProduct);
        when(mapperUtil.toDto(savedProduct)).thenReturn(dto);
        when(jsonUtil.toJson(dto)).thenReturn("{\"id\":1}");
        ResponseEntity<String> response = productController.createProduct(json);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productService, times(1)).create(any());
    }

    @Test
    void updateProduct_ShouldReturnUpdated() throws Exception {
        // Подготовка
        Long id = 1L;
        String json = "{\"name\":\"Обновлено\"}";
        ProductDTO dto = new ProductDTO();
        Product product = new Product();
        Product updatedProduct = new Product();
        when(jsonUtil.fromJson(json, ProductDTO.class)).thenReturn(dto);
        when(validator.validate(dto)).thenReturn(new HashSet<>());
        when(mapperUtil.toEntity(dto)).thenReturn(product);
        when(productService.update(id, product)).thenReturn(updatedProduct);
        when(mapperUtil.toDto(updatedProduct)).thenReturn(dto);
        when(jsonUtil.toJson(dto)).thenReturn("{\"id\":1}");
        String result = productController.updateProduct(id, json);
        assertNotNull(result);
        verify(productService, times(1)).update(eq(id), any());
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() {
        Long id = 1L;
        doNothing().when(productService).delete(id);
        ResponseEntity<Void> response = productController.deleteProduct(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).delete(id);
    }
}