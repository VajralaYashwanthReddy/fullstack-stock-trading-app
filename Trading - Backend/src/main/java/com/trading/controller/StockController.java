package com.trading.controller;

import com.trading.dto.StockQuote;
import com.trading.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // =====================================================
    // ✅ GET LIVE STOCK QUOTE
    // =====================================================
    @GetMapping("/quote/{symbol}")
    public ResponseEntity<StockQuote> getQuote(@PathVariable String symbol) {

        symbol = symbol.toUpperCase();

        StockQuote quote = stockService.getQuote(symbol);

        return ResponseEntity.ok(quote);
    }

    // =====================================================
    // ✅ GET STOCK HISTORY (FOR GRAPH)
    // =====================================================
    @GetMapping("/history/{symbol}")
    public ResponseEntity<?> getHistory(@PathVariable String symbol) {

        symbol = symbol.toUpperCase();

        try {
            return ResponseEntity.ok(
                    stockService.getHistory(symbol)
            );

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Unable to fetch stock history");
        }
    }
}
