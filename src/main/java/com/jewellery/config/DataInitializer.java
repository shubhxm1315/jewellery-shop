package com.jewellery.config;

import com.jewellery.model.Product;
import com.jewellery.model.User;
import com.jewellery.repository.ProductRepository;
import com.jewellery.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeData(
            UserRepository userRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // =========================
            // CREATE ADMIN
            // =========================

            if (userRepository.findByEmail("admin@jewellery.com").isEmpty()) {

                User admin = new User();

                admin.setName("Jewellery Admin");
                admin.setEmail("admin@jewellery.com");
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );
                admin.setPhone("9999999999");
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println("ADMIN CREATED");
            }


            // =========================
            // CREATE CUSTOMER
            // =========================

            if (userRepository.findByEmail("user@jewellery.com").isEmpty()) {

                User user = new User();

                user.setName("Demo Customer");
                user.setEmail("user@jewellery.com");
                user.setPassword(
                        passwordEncoder.encode("user123")
                );
                user.setPhone("8888888888");
                user.setRole("USER");

                userRepository.save(user);

                System.out.println("CUSTOMER CREATED");
            }


            // =========================
            // CREATE PRODUCTS
            // =========================

            if (productRepository.count() == 0) {

                Product p1 = new Product();
                p1.setName("Diamond Gold Necklace");
                p1.setDescription(
                        "Elegant 18K gold necklace with diamond stones."
                );
                p1.setCategory("Necklace");
                p1.setPrice(85000);
                p1.setStock(10);
                p1.setSalesCount(25);
                p1.setNewCollection(true);
                p1.setTrending(true);

                Product p2 = new Product();
                p2.setName("Classic Diamond Ring");
                p2.setDescription(
                        "Beautiful diamond ring for special occasions."
                );
                p2.setCategory("Ring");
                p2.setPrice(45000);
                p2.setStock(15);
                p2.setSalesCount(35);
                p2.setNewCollection(true);
                p2.setTrending(true);

                Product p3 = new Product();
                p3.setName("Gold Earrings");
                p3.setDescription(
                        "Premium gold earrings with elegant design."
                );
                p3.setCategory("Earrings");
                p3.setPrice(25000);
                p3.setStock(20);
                p3.setSalesCount(45);
                p3.setTrending(true);

                Product p4 = new Product();
                p4.setName("Pearl Necklace");
                p4.setDescription(
                        "Elegant pearl necklace for a classic look."
                );
                p4.setCategory("Necklace");
                p4.setPrice(32000);
                p4.setStock(12);
                p4.setSalesCount(20);
                p4.setNewCollection(true);

                Product p5 = new Product();
                p5.setName("Gold Bracelet");
                p5.setDescription(
                        "Stylish gold bracelet with premium finish."
                );
                p5.setCategory("Bracelet");
                p5.setPrice(55000);
                p5.setStock(8);
                p5.setSalesCount(40);
                p5.setTrending(true);

                Product p6 = new Product();
                p6.setName("Diamond Pendant");
                p6.setDescription(
                        "Elegant diamond pendant with gold chain."
                );
                p6.setCategory("Pendant");
                p6.setPrice(40000);
                p6.setStock(18);
                p6.setSalesCount(30);
                p6.setNewCollection(true);

                productRepository.save(p1);
                productRepository.save(p2);
                productRepository.save(p3);
                productRepository.save(p4);
                productRepository.save(p5);
                productRepository.save(p6);

                System.out.println("PRODUCTS CREATED");
            }
        };
    }
}