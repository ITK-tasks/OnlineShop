package itk.onlineshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private List<ProductDTO> products;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private BigDecimal totalPrice;
    private String orderStatus;
}