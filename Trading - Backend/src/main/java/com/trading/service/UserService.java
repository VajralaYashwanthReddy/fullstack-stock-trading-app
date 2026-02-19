package com.trading.service;

import com.trading.dto.LoginRequest;
import com.trading.dto.LoginResponse;
import com.trading.dto.UserRegistrationRequest;
import com.trading.model.User;
import com.trading.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ================= REGISTER =================
    @Transactional
    public User register(UserRegistrationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setBalance(10000.0);

        return userRepository.save(user);
    }

    // ================= LOGIN =================
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBalance(),
                "dummy-jwt-token"
        );
    }

    // ================= GET USER =================
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= ADD FUNDS =================
    @Transactional
    public double addFunds(Long userId, double amount) {

        if (userId == null) {
            throw new RuntimeException("User ID required");
        }

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        User user = getById(userId);

        double newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);

        userRepository.save(user);

        return newBalance;
    }

    // ================= UPDATE PROFILE =================
    @Transactional
    public User updateProfile(Long id, String username, String email) {

        User user = getById(id);

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }

        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }

        return userRepository.save(user);
    }

    // ================= CHANGE PASSWORD =================
    @Transactional
    public void changePassword(Long id, String newPassword) {

        if (newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException("Password cannot be empty");
        }

        User user = getById(id);
        user.setPassword(newPassword);

        userRepository.save(user);
    }

    // ================= GET ALL USERS =================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ================= DELETE =================
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
