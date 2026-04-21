package itk.onlineshop.controller;

import itk.onlineshop.dto.OrderDTO;
import itk.onlineshop.dto.OrderResponseDTO;
import itk.onlineshop.exception.BadRequestException;
import itk.onlineshop.model.Order;
import itk.onlineshop.service.OrderService;
import itk.onlineshop.util.JsonUtil;
import itk.onlineshop.util.MapperUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;
    private final JsonUtil jsonUtil;
    private final Validator validator;
    private final MapperUtil mapperUtil;

    public OrderController(OrderService service, JsonUtil jsonUtil,
                           Validator validator, MapperUtil mapperUtil) {
        this.service = service;
        this.jsonUtil = jsonUtil;
        this.validator = validator;
        this.mapperUtil = mapperUtil;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody String json) throws Exception {
        OrderDTO dto = jsonUtil.fromJson(json, OrderDTO.class);
        Order order = service.create(dto);
        OrderResponseDTO response = mapperUtil.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jsonUtil.toJson(response));
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id) throws Exception {
        Order order = service.get(id);
        OrderResponseDTO response = mapperUtil.toOrderResponseDTO(order);
        return jsonUtil.toJson(response);
    }
}