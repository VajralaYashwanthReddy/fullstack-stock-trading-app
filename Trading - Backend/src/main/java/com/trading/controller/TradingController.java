package com.trading.controller;

import com.trading.dto.*;
import com.trading.model.Transaction;
import com.trading.service.TradingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trading")
@CrossOrigin(origins = "http://localhost:5173")
public class TradingController {

    private final TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    // ==========================================
    // ✅ BUY STOCK
    // ==========================================
    @PostMapping("/buy")
    public ResponseEntity<?> buy(@RequestBody TradeRequest request) {
        try {
            TradeResponse response = tradingService.buyStock(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // 🔴 SELL STOCK
    // ==========================================
    @PostMapping("/sell")
    public ResponseEntity<?> sell(@RequestBody TradeRequest request) {
        try {
            TradeResponse response = tradingService.sellStock(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // 📊 GET PORTFOLIO
    // ==========================================
    @GetMapping("/portfolio/{userId}")
    public ResponseEntity<List<PortfolioResponse>> portfolio(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                tradingService.getPortfolio(userId)
        );
    }

    // ==========================================
    // 📜 GET TRANSACTIONS
    // ==========================================
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> transactions(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                tradingService.getTransactions(userId)
        );
    }
}
