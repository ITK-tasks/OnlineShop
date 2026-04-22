package itk.onlineshop.repository;

import itk.onlineshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
    SELECT o FROM Order o
    JOIN FETCH o.products
    JOIN FETCH o.customer
    WHERE o.orderId = :id
""")
    Optional<Order> findByIdWithRelations(Long id);
}