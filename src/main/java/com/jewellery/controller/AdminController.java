package com.jewellery.controller;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.jewellery.service.EmailService;
import com.jewellery.model.Message;
import com.jewellery.model.Product;
import com.jewellery.repository.MessageRepository;
import com.jewellery.repository.OrderRepository;
import com.jewellery.repository.ProductRepository;
import com.jewellery.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository users;
    private final ProductRepository products;
    private final MessageRepository messages;
    private final OrderRepository orders;
    private final EmailService emailService;

    private final String uploadDir = "src/main/resources/static/images/";

    public AdminController(
            UserRepository users,
            ProductRepository products,
            MessageRepository messages,
            OrderRepository orders,
            EmailService emailService) {

        this.users = users;
        this.products = products;
        this.messages = messages;
        this.orders = orders;
        this.emailService = emailService;
    }

    // =========================
    // ADMIN DASHBOARD
    // =========================

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("users", users.findAll());
        model.addAttribute("products", products.findAll());
        model.addAttribute("messages", messages.findAll());
        model.addAttribute("orders", orders.findAll());

        return "admin";
    }

    // =========================
    // ADD PRODUCT
    // =========================

    @GetMapping("/product/add")
    public String addProduct(Model model) {

        model.addAttribute("product", new Product());

        return "product-form";
    }

    // =========================
    // SAVE PRODUCT
    // =========================

    @PostMapping("/product/save")
    public String saveProduct(
            @ModelAttribute Product product,
            @RequestParam(value = "image", required = false) MultipartFile image)
            throws IOException {


        // If editing an existing product
        if (product.getId() != null && !product.getId().isEmpty()) {

            Product existingProduct = products.findById(product.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Product not found"));

            // Keep existing image if no new image is selected
            if (image == null || image.isEmpty()) {
                product.setImageUrl(existingProduct.getImageUrl());
            }
        }

        // If a new image is selected
        if (image != null && !image.isEmpty()) {

            String fileName = image.getOriginalFilename();

            if (fileName != null && !fileName.isEmpty()) {

                // Save inside static/images
                Path uploadPath = Paths.get(
                        System.getProperty("user.dir"),
                        "src",
                        "main",
                        "resources",
                        "static",
                        "images"
                );

                // Create folder if it doesn't exist
                Files.createDirectories(uploadPath);

                // Save the image
                Path filePath = uploadPath.resolve(fileName);

                Files.write(filePath, image.getBytes());

                // Save image URL in MongoDB
                product.setImageUrl("/images/" + fileName);

                System.out.println("IMAGE SAVED: " + filePath);
                System.out.println("IMAGE URL: " + product.getImageUrl());
            }
        }

        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(new org.bson.types.ObjectId().toHexString());
        }


        System.out.println("ID BEFORE SAVE = [" + product.getId() + "]");
        products.save(product);

        return "redirect:/admin/dashboard";
    }

    // =========================
    // EDIT PRODUCT
    // =========================

    @GetMapping("/product/edit/{id}")
    public String editProduct(
            @PathVariable String id,
            Model model) {

        Product product = products.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        model.addAttribute("product", product);

        return "product-form";
    }

    // =========================
    // DELETE PRODUCT
    // =========================

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(
            @PathVariable String id) {

        products.deleteById(id);

        return "redirect:/admin/dashboard";
    }

    // =========================
    // REPLY TO MESSAGE
    // =========================

    @PostMapping("/message/reply/{id}")
    public String reply(
            @PathVariable String id,
            @RequestParam String reply) {

        Message message = messages.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Message not found"));

        message.setReply(reply);
        message.setStatus("REPLIED");

        messages.save(message);

       // emailService.sendMessageReplyEmail(
         //       message.getEmail(),
           //     reply
       // );

        return "redirect:/admin/dashboard";
    }
}