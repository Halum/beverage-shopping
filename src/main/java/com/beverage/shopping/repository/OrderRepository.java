package com.beverage.shopping.repository;

import com.beverage.shopping.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @EntityGraph(value = "Order.withOrderItems")
    List<Order> findAll();
    
    @EntityGraph(value = "Order.withOrderItems")
    Optional<Order> findById(Long id); 
}
