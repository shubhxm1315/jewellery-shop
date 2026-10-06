package com.jewellery.controller;

import com.jewellery.model.Product;
import com.jewellery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.jewellery.repository.ProductRepository; // (Use your actual repository package name)

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    private final ProductRepository repo;

    public static String UPLOAD_DIRECTORY = "src/main/resources/static/images/";

    public ProductController(ProductRepository r) {
        this.repo = r;
    }

    @GetMapping
    public String products(Model m) {
        m.addAttribute("products", repo.findAll());
        return "products";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, Model m) {

        m.addAttribute("product", repo.findById(id).orElseThrow());

        // Send all products for "You Might Also Like"
        m.addAttribute("products", repo.findAll());

        return "product";
    }

    @GetMapping("/admin/add")
    public String addProductForm(Model m) {
        m.addAttribute("product", new Product());
        return "admin-add";
    }
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }
    @GetMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }

    @PostMapping("/admin/add")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            File directory = new File(UPLOAD_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileName);

            Files.write(fileNameAndPath, file.getBytes());

            product.setImageUrl("/images/" + fileName);
        }

        repo.save(product);
        return "redirect:/products";
    }
}