package com.beverage.shopping.controller;

import com.beverage.shopping.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    @Autowired
    private CartService cartService;
    
    @ModelAttribute
    public void addGlobalAttributes(HttpSession session) {
        session.setAttribute("cartCount", cartService.getTotalItems());
    }
}
