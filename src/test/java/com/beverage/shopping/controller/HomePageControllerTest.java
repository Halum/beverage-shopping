package com.beverage.shopping.controller;

import com.beverage.shopping.model.Bottle;
import com.beverage.shopping.model.Crate;
import com.beverage.shopping.service.BottleService;
import com.beverage.shopping.service.CartService;
import com.beverage.shopping.service.CrateService;
import com.beverage.shopping.service.StockCalculationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomePageController.class)
class HomePageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BottleService bottleService;

    @MockBean
    private CrateService crateService;

    @MockBean
    private CartService cartService;

    @MockBean
    private StockCalculationService stockCalculationService;

    @Test
    void testShowHomePage_Success() throws Exception {
        List<Bottle> bottles = new ArrayList<>();
        List<Crate> crates = new ArrayList<>();

        when(bottleService.getAllBottles()).thenReturn(bottles);
        when(crateService.getAllCrates()).thenReturn(crates);
        when(stockCalculationService.adjustBottleStock(bottles)).thenReturn(bottles);
        when(stockCalculationService.adjustCrateStock(crates)).thenReturn(crates);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attribute("bottles", bottles))
                .andExpect(model().attribute("crates", crates));
    }

    @Test
    void testShowHomePage_EmptyInventory() throws Exception {
        when(bottleService.getAllBottles()).thenReturn(new ArrayList<>());
        when(crateService.getAllCrates()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attribute("bottles", new ArrayList<>()))
                .andExpect(model().attribute("crates", new ArrayList<>()));
    }

    @Test
    void testShowHomePage_ServiceException() throws Exception {
        when(bottleService.getAllBottles()).thenThrow(new RuntimeException("Error fetching bottles"));

        mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful());
    }
}