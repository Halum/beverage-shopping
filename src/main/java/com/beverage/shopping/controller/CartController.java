package com.beverage.shopping.controller;

import com.beverage.shopping.model.Bottle;
import com.beverage.shopping.model.Crate;
import com.beverage.shopping.model.Order;
import com.beverage.shopping.model.OrderItem;
import com.beverage.shopping.model.Beverage;
import com.beverage.shopping.service.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/cart")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private BottleService bottleService;

    @Autowired
    private CrateService crateService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        
        return "cart/checkout";
    }

    // Correct placeholders in the URL
    @PostMapping("/add/{beverageId}")
    public String addToCart(@PathVariable Long beverageId, 
                          @RequestParam String type,
                          RedirectAttributes redirectAttributes) {
        logger.info("Adding beverage with id {} and type {} to cart", beverageId, type);

        Optional<Beverage> beverageOpt = findBeverage(beverageId, type);

        if (beverageOpt.isPresent()) {
            Beverage beverage = beverageOpt.get();
            cartService.addToCart(beverage);
            redirectAttributes.addFlashAttribute("successMessage", 
                String.format("%s added to cart!", beverage.getName()));
        } else {
            logger.warn("Beverage with id {} not found", beverageId);
            redirectAttributes.addFlashAttribute("errorMessage", "Item not found!");
        }

        return "redirect:/";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        
        return "cart/checkout";
    }

    @PostMapping("/remove/{beverageId}")
    public String removeFromCart(@PathVariable Long beverageId, 
                               @RequestParam String type,
                               RedirectAttributes redirectAttributes) {
        Optional<Beverage> beverageOpt = findBeverage(beverageId, type);

        if (beverageOpt.isPresent()) {
            Beverage beverage = beverageOpt.get();
            cartService.removeFromCart(beverage);
            redirectAttributes.addFlashAttribute("successMessage", 
                String.format("%s removed from cart!", beverage.getName()));
        }
        return "redirect:/cart/checkout";
    }

    @PostMapping("/increase/{beverageId}")
    public String increaseQuantity(@PathVariable Long beverageId,
                                 @RequestParam String type,
                                 RedirectAttributes redirectAttributes) {
        Optional<Beverage> beverageOpt = findBeverage(beverageId, type);

        if (beverageOpt.isPresent()) {
            Beverage beverage = beverageOpt.get();
            cartService.increaseQuantity(beverage);
            redirectAttributes.addFlashAttribute("successMessage", 
                String.format("Increased quantity of %s!", beverage.getName()));
        }
        return "redirect:/cart/checkout";
    }

    @PostMapping("/decrease/{beverageId}")
    public String decreaseQuantity(@PathVariable Long beverageId,
                                 @RequestParam String type,
                                 RedirectAttributes redirectAttributes) {
        Optional<Beverage> beverageOpt = findBeverage(beverageId, type);

        if (beverageOpt.isPresent()) {
            Beverage beverage = beverageOpt.get();
            cartService.decreaseQuantity(beverage);
            redirectAttributes.addFlashAttribute("successMessage", 
                String.format("Decreased quantity of %s!", beverage.getName()));
        }
        return "redirect:/cart/checkout";
    }

    private Optional<Beverage> findBeverage(Long id, String type) {
        if ("bottle".equals(type)) {
            return bottleService.getBottleById(id).map(bottle -> bottle);
        } else if ("crate".equals(type)) {
            return crateService.getCrateById(id).map(crate -> crate);
        }
        return Optional.empty();
    }
}