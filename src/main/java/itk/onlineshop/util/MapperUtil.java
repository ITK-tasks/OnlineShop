package itk.onlineshop.util;

import itk.onlineshop.dto.OrderResponseDTO;
import itk.onlineshop.dto.ProductDTO;
import itk.onlineshop.model.Order;
import itk.onlineshop.model.Product;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    public ProductDTO toDto(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(p.getProductId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setQuantityInStock(p.getQuantityInStock());
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        Product p = new Product();
        p.setProductId(dto.getProductId());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setQuantityInStock(dto.getQuantityInStock());
        return p;
    }

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomer().getCustomerId());
        dto.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        dto.setProducts(order.getProducts().stream()
                .map(this::toDto)
                .toList());
        dto.setOrderDate(order.getOrderDate());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderStatus(order.getOrderStatus());
        return dto;
    }
}