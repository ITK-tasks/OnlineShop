package itk.onlineshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTO {

    private Long orderId;

    @NotNull
    private Long customerId;

    @NotEmpty
    private List<Long> productIds;

    @NotBlank
    private String shippingAddress;

}
