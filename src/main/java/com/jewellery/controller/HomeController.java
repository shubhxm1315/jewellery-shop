package com.jewellery.controller;

import com.jewellery.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductRepository products;

    public HomeController(ProductRepository products) {
        this.products = products;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("newProducts",
                products.findByNewCollectionTrue());

        model.addAttribute("trending",
                products.findByTrendingTrue());

        model.addAttribute("best",
                products.findTop6ByOrderBySalesCountDesc());

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}