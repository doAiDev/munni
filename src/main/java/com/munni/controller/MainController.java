package com.munni.controller;

import com.munni.model.Product;
import com.munni.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final ProductService productService;

    public MainController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", Product.Category.values());
        model.addAttribute("selectedCategory", null);
        return "index";
    }

    @GetMapping("/gallery")
    public String gallery(@RequestParam(required = false) String category, Model model) {
        Product.Category cat = null;
        if (category != null && !category.isBlank()) {
            try { cat = Product.Category.valueOf(category); } catch (IllegalArgumentException ignored) {}
        }
        var products = (cat != null) ? productService.findByCategory(cat) : productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", Product.Category.values());
        model.addAttribute("selectedCategory", cat);
        return "gallery";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
