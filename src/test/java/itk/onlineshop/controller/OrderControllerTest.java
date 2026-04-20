package itk.onlineshop.controller;

import itk.onlineshop.dto.OrderDTO;
import itk.onlineshop.dto.OrderResponseDTO;
import itk.onlineshop.model.Order;
import itk.onlineshop.service.OrderService;
import itk.onlineshop.util.JsonUtil;
import itk.onlineshop.util.MapperUtil;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private JsonUtil jsonUtil;

    @Mock
    private Validator validator;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private OrderController orderController;

    @Test
    void createOrder_ShouldReturnCreated() throws Exception {
        String json = "{\"customerId\":1,\"productIds\":[1,2],\"shippingAddress\":\"Москва\"}";
        OrderDTO dto = new OrderDTO();
        Order order = new Order();
        OrderResponseDTO responseDto = new OrderResponseDTO();
        when(jsonUtil.fromJson(json, OrderDTO.class)).thenReturn(dto);
        when(validator.validate(dto)).thenReturn(new HashSet<>());
        when(orderService.create(dto)).thenReturn(order);
        when(mapperUtil.toOrderResponseDTO(order)).thenReturn(responseDto);
        when(jsonUtil.toJson(responseDto)).thenReturn("{\"orderId\":1}");
        ResponseEntity<String> response = orderController.createOrder(json);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(orderService, times(1)).create(any());
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        Long id = 1L;
        Order order = new Order();
        OrderResponseDTO responseDto = new OrderResponseDTO();
        when(orderService.get(id)).thenReturn(order);
        when(mapperUtil.toOrderResponseDTO(order)).thenReturn(responseDto);
        when(jsonUtil.toJson(responseDto)).thenReturn("{\"orderId\":1}");
        String result = orderController.getOrder(id);
        assertNotNull(result);
        verify(orderService, times(1)).get(id);
    }
}