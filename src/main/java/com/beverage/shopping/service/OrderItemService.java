package com.beverage.shopping.service;

import com.beverage.shopping.model.OrderItem;
import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    List<OrderItem> getAllOrderItems();
    Optional<OrderItem> getOrderItemById(Long id);
    OrderItem saveOrderItem(OrderItem orderItem);
    void deleteOrderItem(Long id);
}
