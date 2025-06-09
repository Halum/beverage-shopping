package com.beverage.shopping.service;

import com.beverage.shopping.model.Order;
import com.beverage.shopping.model.Beverage;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Order saveOrder(Order order);
    void deleteOrder(Long id);
    Order createOrderFromCart(Map<Beverage, Integer> cartItems, double totalPrice);
}
