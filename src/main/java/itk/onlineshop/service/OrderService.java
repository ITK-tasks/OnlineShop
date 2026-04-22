package itk.onlineshop.service;

import itk.onlineshop.dto.OrderDTO;
import itk.onlineshop.exception.BadRequestException;
import itk.onlineshop.exception.NotFoundException;
import itk.onlineshop.model.Customer;
import itk.onlineshop.model.Order;
import itk.onlineshop.model.Product;
import itk.onlineshop.repository.CustomerRepository;
import itk.onlineshop.repository.OrderRepository;
import itk.onlineshop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;

    public OrderService(OrderRepository orderRepo,
                        ProductRepository productRepo,
                        CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
    }

    public Order create(OrderDTO dto) {
        Customer customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Покупатель не найден"));
        List<Product> products = productRepo.findAllById(dto.getProductIds());
        if (products.size() != dto.getProductIds().size()) {
            throw new NotFoundException("Некоторые продукты не найдены");
        }
        Map<Long, Long> productCount = dto.getProductIds().stream()
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));
        for (Product product : products) {
            long count = productCount.get(product.getProductId());
            if (product.getQuantityInStock() < count) {
                throw new BadRequestException("Недостаточно товара: " + product.getName());
            }
            product.setQuantityInStock(product.getQuantityInStock() - (int) count);
        }
        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(products);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(dto.getShippingAddress());
        order.setOrderStatus("NEW");
        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);
        return orderRepo.save(order);
    }

    @Transactional(readOnly = true)
    public Order get(Long id) {
        return orderRepo.findByIdWithRelations(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
    }
}