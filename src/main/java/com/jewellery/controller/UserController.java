package com.jewellery.controller;

// Make sure ALL of these imports are at the top of your file!

import com.jewellery.model.User;
import com.jewellery.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.jewellery.model.PasswordResetToken;
import com.jewellery.repository.PasswordResetTokenRepository;
import com.jewellery.service.EmailService;

import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Controller
public class UserController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    public UserController(UserRepository r, PasswordEncoder e,
                          PasswordResetTokenRepository tokenRepository,
                          EmailService emailService) {
        repo = r;
        encoder = e;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/register")
    public String form(Model m) {
        m.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestParam String email,
            Model model) {

        User user = repo.findByEmail(email).orElse(null);

        if (user == null) {
            model.addAttribute(
                    "error",
                    "No account is registered with this email."
            );
            return "forgot-password";
        }

        // Remove any previous reset token for this email
        tokenRepository.deleteByEmail(email);

        // Generate secure random token
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        StringBuilder tokenBuilder = new StringBuilder();

        for (byte b : bytes) {
            tokenBuilder.append(String.format("%02x", b));
        }

        String token = tokenBuilder.toString();

        // Token expires after 15 minutes
        Instant expiryDate =
                Instant.now().plus(15, ChronoUnit.MINUTES);

        PasswordResetToken resetToken =
                new PasswordResetToken(
                        email,
                        token,
                        expiryDate
                );

        tokenRepository.save(resetToken);

        // Send reset link
        emailService.sendPasswordResetEmail(email, token);

        model.addAttribute(
                "message",
                "A password reset link has been sent to your email."
        );

        return "forgot-password";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {

        // This checks the Optional<User> from your updated Repository
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "An account with this email already exists.");
            return "register";
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");
        repo.save(user);

        return "redirect:/login";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(
            @RequestParam String token,
            Model model) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null) {
            model.addAttribute("error", "Invalid or expired reset link.");
            return "forgot-password";
        }

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            model.addAttribute("error", "This reset link has expired.");
            return "forgot-password";
        }

        model.addAttribute("token", token);

        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null ||
                resetToken.getExpiryDate().isBefore(Instant.now())) {

            model.addAttribute("error", "Invalid or expired reset link.");
            return "forgot-password";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", token);
            return "reset-password";
        }

        User user = repo.findByEmail(resetToken.getEmail()).orElse(null);

        if (user == null) {
            model.addAttribute("error", "User account not found.");
            return "forgot-password";
        }

        user.setPassword(encoder.encode(password));
        repo.save(user);

        tokenRepository.deleteByToken(token);

        return "redirect:/login?resetSuccess=true";
    }
}