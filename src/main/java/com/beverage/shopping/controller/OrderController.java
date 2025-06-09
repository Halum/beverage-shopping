package com.beverage.shopping.controller;

import com.beverage.shopping.model.Beverage;
import com.beverage.shopping.service.CartService;
import com.beverage.shopping.service.OrderService;
import com.beverage.shopping.exception.InsufficientStockException;

import jakarta.transaction.Transactional;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        
        return "order/list";
    }

    @PostMapping("/place")
    @Transactional
    public String  placeOrder(RedirectAttributes redirectAttributes) {
        try {
            Map<Beverage, Integer> cartItems = cartService.getCartItems();
            
            if (cartItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty");
                return "redirect:/cart/checkout";
            }

            double totalPrice = cartService.getTotal();
            orderService.createOrderFromCart(cartItems, totalPrice);
            
            cartService.clearCart();

            redirectAttributes.addFlashAttribute("successMessage", 
                "Order placed successfully!");
            return "redirect:/";
            
        } catch (InsufficientStockException e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                String.format("Cannot place order: %s has insufficient stock. Available: %d, Requested: %d", 
                    e.getBeverageName(), e.getAvailable(), e.getRequested()));
            return "redirect:/cart/checkout";
        } catch (Exception e) {
            logger.error("Error placing order", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "An error occurred while placing your order: " + e.getMessage());
            return "redirect:/cart/checkout";
        }
    }
}
