package itk.onlineshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {

    private Long productId;

    @NotBlank(message = "Имя - обязательно")
    private String name;

    private String description;

    @NotNull(message = "Цена - обязательно.")
    @Positive(message = "Цена должна быть больше нуля.")
    private BigDecimal price;

    @NotNull(message = "Колличество на складе - обязательно.")
    @Min(value = 0, message = "Колличество на складе должно быть >= 0")
    private Integer quantityInStock;
}