package com.beverage.shopping.service.impl;

import com.beverage.shopping.model.*;
import com.beverage.shopping.repository.OrderRepository;
import com.beverage.shopping.repository.OrderItemRepository;
import com.beverage.shopping.repository.BottleRepository;
import com.beverage.shopping.repository.CrateRepository;
import com.beverage.shopping.service.OrderService;
import com.beverage.shopping.exception.InsufficientStockException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BottleRepository bottleRepository;
    private final CrateRepository crateRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, 
                          OrderItemRepository orderItemRepository,
                          BottleRepository bottleRepository,
                          CrateRepository crateRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.bottleRepository = bottleRepository;
        this.crateRepository = crateRepository;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Order createOrderFromCart(Map<Beverage, Integer> cartItems, double totalPrice) {
        // First validate stock levels for all items with fresh data
        Map<Long, Beverage> freshBeverages = new HashMap<>();
        
        for (Map.Entry<Beverage, Integer> entry : cartItems.entrySet()) {
            Beverage beverage = entry.getKey();
            int quantity = entry.getValue();
            
            // Get fresh data from database based on type and ID
            Beverage freshBeverage;

            if (beverage instanceof Bottle) {
                Bottle bottle = (Bottle) beverage;
                freshBeverage = bottleRepository.findById(bottle.getId())
                    .orElseThrow(() -> new IllegalStateException("Bottle not found: " + bottle.getId()));
            } else if (beverage instanceof Crate) {
                Crate crate = (Crate) beverage;
                freshBeverage = crateRepository.findById(crate.getId())
                    .orElseThrow(() -> new IllegalStateException("Crate not found: " + crate.getId()));
            } else {
                throw new IllegalStateException("Unknown beverage type");
            }

            // Verify stock with fresh data
            if (freshBeverage.getInStock() < quantity) {
                throw new InsufficientStockException(
                    freshBeverage.getName(),
                    freshBeverage.getInStock(),
                    quantity
                );
            }
            
            freshBeverages.put(freshBeverage.getId(), freshBeverage);
        }

        // Create and save the order
        Order order = orderRepository.save(Order.builder()
                .price(totalPrice)
                .orderItems(new ArrayList<>())
                .build());

        // Create order items and update stock levels
        AtomicInteger position = new AtomicInteger(0);
        
        cartItems.forEach((beverage, quantity) -> {
            Beverage freshBeverage = freshBeverages.get(beverage.getId());
            
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .quantity(quantity)
                    .position(position.getAndIncrement())
                    .price(freshBeverage.getPrice() * quantity)
                    .productId(freshBeverage.getId())
                    .productType(freshBeverage instanceof Bottle ? "BOTTLE" : "CRATE")
                    .productName(freshBeverage.getName())
                    .build();
            
            // Update stock levels using fresh data
            if (freshBeverage instanceof Bottle) {
                Bottle freshBottle = (Bottle) freshBeverage;
                freshBottle.setInStock(freshBottle.getInStock() - quantity);
                bottleRepository.save(freshBottle);
            } else if (freshBeverage instanceof Crate) {
                Crate freshCrate = (Crate) freshBeverage;
                freshCrate.setInStock(freshCrate.getInStock() - quantity);
                crateRepository.save(freshCrate);
            }
            
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        });

        return order;
    }

    @Transactional
    public void removeOrderItem(Long orderId, Long orderItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + orderItemId));

        if (!order.getOrderItems().contains(orderItem)) {
            throw new IllegalStateException("OrderItem does not belong to the specified order");
        }

        order.getOrderItems().remove(orderItem);
        orderRepository.save(order);
        orderItemRepository.delete(orderItem);
    }

    @Transactional
    public void updateOrderItemQuantity(Long orderId, Long orderItemId, int newQuantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + orderItemId));

        if (!order.getOrderItems().contains(orderItem)) {
            throw new IllegalStateException("OrderItem does not belong to the specified order");
        }

        Beverage product = null;
        if ("BOTTLE".equals(orderItem.getProductType())) {
            product = bottleRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Bottle not found with id: " + orderItem.getProductId()));
        } else if ("CRATE".equals(orderItem.getProductType())) {
            product = crateRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Crate not found with id: " + orderItem.getProductId()));
        }

        if (product != null && product.getInStock() < newQuantity) {
            throw new IllegalStateException("Insufficient stock for " + product.getName());
        }

        orderItem.setQuantity(newQuantity);
        orderItem.setPrice(product.getPrice() * newQuantity);
        orderItemRepository.save(orderItem);
    }
}
