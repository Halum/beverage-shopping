package com.beverage.shopping.controller;

import com.beverage.shopping.model.Bottle;
import com.beverage.shopping.model.Crate;
import com.beverage.shopping.service.BottleService;
import com.beverage.shopping.service.CartService;
import com.beverage.shopping.service.CrateService;
import com.beverage.shopping.service.StockCalculationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    private BottleService bottleService;

    @Autowired
    private CrateService crateService;

    @Autowired
    private CartService cartService;
    
    @Autowired
    private StockCalculationService stockCalculationService;

    /**
     * Home page handler.
     * Displays all available bottles and crates with adjusted stock levels.
     *
     * @param model The model to pass attributes to the view.
     * @return The view name for the home page.
     */
    @GetMapping("/")
    public String showHomePage(Model model) {
        try {
            // Fetch all bottles and crates from the service layer
            List<Bottle> bottles = bottleService.getAllBottles();
            List<Crate> crates = crateService.getAllCrates();

            // Adjust stock levels based on cart contents
            List<Crate> adjustedCrates = stockCalculationService.adjustCrateStock(crates);
            List<Bottle> adjustedBottles = stockCalculationService.adjustBottleStock(bottles);

            // Add data to the model
            model.addAttribute("bottles", adjustedBottles);
            model.addAttribute("crates", adjustedCrates);

            // Return the view name (index.html)
            return "home/index";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while loading data: " + e.getMessage());
            return "home/index"; // Return the same page with an error message
        }
    }
}
