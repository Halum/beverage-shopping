package com.beverage.shopping.controller;

import com.beverage.shopping.model.Beverage;
import com.beverage.shopping.model.Bottle;
import com.beverage.shopping.model.Order;
import com.beverage.shopping.service.CartService;
import com.beverage.shopping.service.OrderService;
import com.beverage.shopping.exception.InsufficientStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CartService cartService;

    @MockBean
    private OrderService orderService;

    private MockMvc mockMvc;
    private Bottle testBottle;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create test bottle
        testBottle = Bottle.builder()
                .id(1L)
                .name("Coke")
                .price(1.5)
                .inStock(100)
                .volume(500.0)
                .volumePercent(0.0)
                .supplier("Coca Cola")
                .build();

        // Reset mocks
        reset(cartService, orderService);
    }

    @Test
    void testPlaceOrder_Success() throws Exception {
        // Arrange
        Map<Beverage, Integer> mockCart = new HashMap<>();
        mockCart.put(testBottle, 2);
        
        when(cartService.getCartItems()).thenReturn(mockCart);
        when(cartService.getTotal()).thenReturn(3.0);
        when(orderService.createOrderFromCart(mockCart, 3.0)).thenReturn(new Order());
        doNothing().when(cartService).clearCart();

        // Act & Assert
        mockMvc.perform(post("/orders/place"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("successMessage", "Order placed successfully!"));

        // Verify service interactions
        verify(orderService).createOrderFromCart(mockCart, 3.0);
        verify(cartService).clearCart();
    }

    @Test
    void testPlaceOrder_EmptyCart() throws Exception {
        // Arrange
        when(cartService.getCartItems()).thenReturn(new HashMap<>());
        
        // Act & Assert
        mockMvc.perform(post("/orders/place"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/checkout"))
                .andExpect(flash().attribute("errorMessage", "Your cart is empty"));

        // Verify no service interactions
        verify(orderService, never()).createOrderFromCart(any(), anyDouble());
        verify(cartService, never()).clearCart();
    }

    @Test
    void testPlaceOrder_InsufficientStock() throws Exception {
        // Arrange
        Map<Beverage, Integer> mockCart = new HashMap<>();
        mockCart.put(testBottle, 2);

        when(cartService.getCartItems()).thenReturn(mockCart);
        when(cartService.getTotal()).thenReturn(3.0);
        when(orderService.createOrderFromCart(mockCart, 3.0))
            .thenThrow(new InsufficientStockException("Coke", 1, 2));
        
        // Act & Assert
        mockMvc.perform(post("/orders/place"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/checkout"))
                .andExpect(flash().attribute("errorMessage", 
                    "Cannot place order: Coke has insufficient stock. Available: 1, Requested: 2"));

        // Verify service interactions
        verify(orderService).createOrderFromCart(mockCart, 3.0);
        verify(cartService, never()).clearCart();
    }

    @Test
    void testPlaceOrder_GenericException() throws Exception {
        // Arrange
        Map<Beverage, Integer> mockCart = new HashMap<>();
        mockCart.put(testBottle, 1);
        
        when(cartService.getCartItems()).thenReturn(mockCart);
        when(cartService.getTotal()).thenReturn(1.5);
        when(orderService.createOrderFromCart(mockCart, 1.5))
            .thenThrow(new RuntimeException("Test error"));

        // Act & Assert
        mockMvc.perform(post("/orders/place"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/checkout"))
                .andExpect(flash().attribute("errorMessage", 
                    "An error occurred while placing your order: Test error"));

        // Verify service interactions
        verify(orderService).createOrderFromCart(mockCart, 1.5);
        verify(cartService, never()).clearCart();
    }
}
