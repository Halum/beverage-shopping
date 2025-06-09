package com.beverage.shopping.controller;
import com.beverage.shopping.exception.InsufficientStockException;
import com.beverage.shopping.model.Beverage;
import com.beverage.shopping.service.CartService;

import com.beverage.shopping.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CartService cartService;

    @Test
    void testPlaceOrder_Success() throws Exception {
        Map<Beverage, Integer> cartItems = new HashMap<>();
        Beverage beverage = mock(Beverage.class);
        cartItems.put(beverage, 1);

        when(cartService.getCartItems()).thenReturn(cartItems);
        when(cartService.getTotal()).thenReturn(100.0);

        mockMvc.perform(post("/orders/place"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("successMessage", "Order placed successfully!"));

        verify(orderService, times(1)).createOrderFromCart(cartItems, 100.0);
        verify(cartService, times(1)).clearCart();
    }

    @Test
    void testPlaceOrder_EmptyCart() throws Exception {
        when(cartService.getCartItems()).thenReturn(new HashMap<>());

        mockMvc.perform(post("/orders/place"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/checkout"))
                .andExpect(flash().attribute("errorMessage", "Your cart is empty"));
    }

    @Test
    void testPlaceOrder_InsufficientStock() throws Exception {
        // Arrange: Set up cart with items
        Map<Beverage, Integer> cartItems = new HashMap<>();
        Beverage beverage = mock(Beverage.class);
        cartItems.put(beverage, 10);

        when(cartService.getCartItems()).thenReturn(cartItems);
        when(cartService.getTotal()).thenReturn(100.0);

        // Simulate InsufficientStockException
        doThrow(new InsufficientStockException("Test Beverage", 5, 10)).when(orderService)
                .createOrderFromCart(cartItems, 100.0);

        // Act & Assert
        mockMvc.perform(post("/orders/place"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/checkout"))
                .andExpect(flash().attribute(
                        "errorMessage", "Cannot place order: Test Beverage has insufficient stock. Available: 5, Requested: 10"));
    }

    @Test
    void testPlaceOrder_GenericException() throws Exception {
        // Arrange: Set up cart with items
        Map<Beverage, Integer> cartItems = new HashMap<>();
        Beverage beverage = mock(Beverage.class);
        cartItems.put(beverage, 2);

        when(cartService.getCartItems()).thenReturn(cartItems);
        when(cartService.getTotal()).thenReturn(50.0);

        // Simulate a generic RuntimeException
        doThrow(new RuntimeException("Unexpected Error")).when(orderService).createOrderFromCart(cartItems, 50.0);

        // Act & Assert
        mockMvc.perform(post("/orders/place"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/checkout"))
                .andExpect(flash().attribute("errorMessage", "An error occurred while placing your order: Unexpected Error"));
    }
}
