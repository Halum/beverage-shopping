package com.beverage.shopping.controller;

import com.beverage.shopping.model.Bottle;
import com.beverage.shopping.model.Crate;
import com.beverage.shopping.service.BottleService;
import com.beverage.shopping.service.CartService;
import com.beverage.shopping.service.CrateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Controller
@RequestMapping("/manage")
public class BeverageManagementController {
    
    private static final Logger logger = LoggerFactory.getLogger(BeverageManagementController.class);

    @Autowired
    private BottleService bottleService;

    @Autowired
    private CrateService crateService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String showManagementPage(Model model) {
        model.addAttribute("bottles", bottleService.getAllBottles());
        model.addAttribute("crates", crateService.getAllCrates());

        return "beverage/manage";
    }

    @GetMapping("/bottle/add")
    public String addBottle(Model model) {
        model.addAttribute("bottle", new Bottle());

        return "beverage/bottle-form";
    }

    @GetMapping("/bottle/{id}/update")
    public String updateBottle(@PathVariable Long id, Model model) {
        Optional<Bottle> bottleOpt = bottleService.getBottleById(id);

        if (bottleOpt.isPresent()) {
            model.addAttribute("bottle", bottleOpt.get());
        } else {
            model.addAttribute("errorMessage", "Bottle not found");
        }
        
        return "beverage/bottle-form";
    }

    @PostMapping("/bottle/save")
    public String saveBottle(@Valid @ModelAttribute("bottle") Bottle bottle, 
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        logger.info("Attempting to save bottle: {}", bottle);

        if (result.hasErrors()) {
            logger.error("Validation errors for bottle: {}", result.getAllErrors());
            return "beverage/bottle-form";
        }

        try {
            boolean isNew = bottle.getId() == 0;
            bottleService.saveBottle(bottle);
            redirectAttributes.addFlashAttribute("successMessage", 
                isNew ? "Bottle added successfully!" : "Bottle updated successfully!");
            logger.info("Successfully saved bottle: {}", bottle);
        } catch (Exception e) {
            logger.error("Error saving bottle: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving bottle: " + e.getMessage());
        }
        return "redirect:/manage";
    }

    @PostMapping("/bottle/{id}/delete")
    public String deleteBottle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bottleService.deleteBottle(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bottle deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting bottle: " + e.getMessage());
        }
        return "redirect:/manage";
    }

    @GetMapping("/crate/add")
    public String addCrate(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("crate", new Crate());

        return "beverage/crate-form";
    }

    @GetMapping("/crate/{id}/update")
    public String updateCrate(@PathVariable Long id, Model model) {
        Optional<Crate> crateOpt = crateService.getCrateById(id);

        if (crateOpt.isPresent()) {
            model.addAttribute("crate", crateOpt.get());
        } else {
            model.addAttribute("errorMessage", "Crate not found");
        }
        
        return "beverage/crate-form";
    }

    @PostMapping("/crate/save")
    public String saveCrate(@Valid @ModelAttribute("crate") Crate crate,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        logger.info("Attempting to save crate: {}", crate);
        if (result.hasErrors()) {
            logger.error("Validation errors for crate: {}", result.getAllErrors());
            return "beverage/crate-form";
        }

        try {
            boolean isNew = crate.getId() == 0;
            crateService.saveCrate(crate);
            redirectAttributes.addFlashAttribute("successMessage", 
                isNew ? "Crate added successfully!" : "Crate updated successfully!");
            logger.info("Successfully saved crate: {}", crate);
        } catch (Exception e) {
            logger.error("Error saving crate: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving crate: " + e.getMessage());
        }
        return "redirect:/manage";
    }

    @PostMapping("/crate/{id}/delete")
    public String deleteCrate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            crateService.deleteCrate(id);
            redirectAttributes.addFlashAttribute("successMessage", "Crate deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting crate: " + e.getMessage());
        }
        return "redirect:/manage";
    }
}
