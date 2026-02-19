package com.trading.controller;

import com.trading.dto.*;
import com.trading.model.User;
import com.trading.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =====================================================
    // ✅ REGISTER
    // =====================================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
        try {
            User user = userService.register(request);

            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "balance", user.getBalance()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // =====================================================
    // ✅ LOGIN
    // =====================================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(userService.login(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // =====================================================
    // ✅ GET USER BY ID
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            User user = userService.getById(id);

            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "balance", user.getBalance()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // =====================================================
    // ✅ ADD FUNDS
    // =====================================================
    @PostMapping("/add-funds")
    public ResponseEntity<?> addFunds(@Valid @RequestBody AddFundsRequest request) {
        try {

            double updatedBalance =
                    userService.addFunds(request.getUserId(), request.getAmount());

            return ResponseEntity.ok(Map.of(
                    "message", "Funds added successfully",
                    "balance", updatedBalance
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // =====================================================
    // ✅ UPDATE PROFILE (FIXED)
    // =====================================================
    @PutMapping("/update-profile/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileRequest request
    ) {
        try {

            User updatedUser = userService.updateProfile(
                    id,
                    request.getUsername(),
                    request.getEmail()
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Profile updated successfully",
                    "id", updatedUser.getId(),
                    "username", updatedUser.getUsername(),
                    "email", updatedUser.getEmail(),
                    "balance", updatedUser.getBalance()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // =====================================================
    // ✅ CHANGE PASSWORD (FIXED)
    // =====================================================
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request
    ) {
        try {

            userService.changePassword(
                    request.getUserId(),
                    request.getNewPassword()
            );

            return ResponseEntity.ok(
                    Map.of("message", "Password updated successfully")
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
