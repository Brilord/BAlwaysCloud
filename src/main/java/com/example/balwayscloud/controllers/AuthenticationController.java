package com.example.balwayscloud.controllers;

import com.example.balwayscloud.model.User;
import com.example.balwayscloud.repository.CustomerRepository;
import com.example.balwayscloud.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    TokenService tokenService,
                                    CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public String register(@RequestBody User customer) {
        if (customer.getName() == null || customer.getEmail() == null || customer.getPassword() == null) {
            return "All fields are required";
        }

        String hashedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(hashedPassword);

        try {
            customerRepository.save(customer);
            return "User registered";
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody User customer) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    customer.getEmail(),
                                    customer.getPassword()));

            return tokenService.generateToken(authentication);
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password: " + e.getMessage());
        }
    }

    @GetMapping("/getUsername/{id}")
    public String getUsername(@PathVariable("id") Long id) throws IOException {
        Optional<User> user = customerRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getName();
        } else {
            return "User not found";
        }
    }

    @PostMapping("/changePassword/{id}")
    public String changePassword(@PathVariable("id") Long id, @RequestBody User customer) {
        try {
            Optional<User> existingUser = customerRepository.findById(id);
            if (existingUser.isPresent()) {
                User userToUpdate = existingUser.get();
                userToUpdate.setPassword(passwordEncoder.encode(customer.getPassword()));
                customerRepository.save(userToUpdate);
                return "Password changed successfully";
            } else {
                return "User not found";
            }
        } catch (Exception e) {
            throw new RuntimeException("Error changing password: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAccount/{id}")
    public String deleteAccount(@PathVariable("id") Long id) {
        try {
            customerRepository.deleteById(id);
            return "Account deleted successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting account: " + e.getMessage());
        }
    }
}
