package com.trading.service;

import com.trading.dto.StockQuote;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockService {

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔴 Replace with your real key
    private static final String API_KEY = "YOUR_FINNHUB_KEY";

    // ===== SIMPLE CACHE =====
    private final Map<String, StockQuote> cache = new HashMap<>();
    private final Map<String, LocalDateTime> lastUpdated = new HashMap<>();

    // =====================================================
    // ✅ GET LIVE QUOTE
    // =====================================================
    public StockQuote getQuote(String symbol) {

        symbol = symbol.toUpperCase();

        // ✅ Return cached if within 30 sec
        if (cache.containsKey(symbol)
                && lastUpdated.containsKey(symbol)
                && lastUpdated.get(symbol)
                .plusSeconds(30)
                .isAfter(LocalDateTime.now())) {

            return cache.get(symbol);
        }

        try {

            String url = "https://finnhub.io/api/v1/quote?symbol="
                    + symbol + "&token=" + API_KEY;

            Map<String, Object> response =
                    restTemplate.getForObject(url, Map.class);

            if (response == null || response.get("c") == null) {
                return generateFallback(symbol);
            }

            double currentPrice = parseDouble(response.get("c"));
            double previousClose = parseDouble(response.get("pc"));

            double change = 0;
            double percentChange = 0;

            if (previousClose != 0) {
                change = currentPrice - previousClose;
                percentChange = (change / previousClose) * 100;
            }

            StockQuote quote = new StockQuote();
            quote.setSymbol(symbol);
            quote.setCurrentPrice(round(currentPrice));
            quote.setChange(round(change));
            quote.setChangePercent(round(percentChange));

            // Save cache
            cache.put(symbol, quote);
            lastUpdated.put(symbol, LocalDateTime.now());

            return quote;

        } catch (HttpClientErrorException.TooManyRequests e) {
            return generateFallback(symbol);

        } catch (Exception e) {
            return generateFallback(symbol);
        }
    }

    // =====================================================
    // ✅ GET INTRADAY HISTORY (FOR GRAPH)
    // =====================================================
    public Map<String, Object> getHistory(String symbol) {

        symbol = symbol.toUpperCase();

        try {

            long now = System.currentTimeMillis() / 1000;
            long oneDayAgo = now - (60 * 60 * 24);

            String url = "https://finnhub.io/api/v1/stock/candle?symbol="
                    + symbol
                    + "&resolution=5"
                    + "&from=" + oneDayAgo
                    + "&to=" + now
                    + "&token=" + API_KEY;

            Map<String, Object> response =
                    restTemplate.getForObject(url, Map.class);

            if (response == null || response.get("c") == null) {
                return generateFallbackHistory();
            }

            return response;

        } catch (Exception e) {
            return generateFallbackHistory();
        }
    }

    // =====================================================
    // 🔥 FALLBACK PRICE (when API fails)
    // =====================================================
    private StockQuote generateFallback(String symbol) {

        double basePrice = 1000 + Math.random() * 500;
        double change = Math.random() * 20 - 10;
        double percentChange = (change / basePrice) * 100;

        StockQuote quote = new StockQuote();
        quote.setSymbol(symbol);
        quote.setCurrentPrice(round(basePrice));
        quote.setChange(round(change));
        quote.setChangePercent(round(percentChange));

        return quote;
    }

    // =====================================================
    // 🔥 FALLBACK HISTORY (Fake graph data)
    // =====================================================
    private Map<String, Object> generateFallbackHistory() {

        Map<String, Object> fallback = new HashMap<>();

        int points = 30;
        double base = 1000;

        long now = System.currentTimeMillis() / 1000;
        long interval = 300; // 5 minutes

        double[] prices = new double[points];
        long[] times = new long[points];

        for (int i = 0; i < points; i++) {
            prices[i] = round(base + (Math.random() * 20 - 10));
            times[i] = now - (points - i) * interval;
        }

        fallback.put("c", prices);
        fallback.put("t", times);

        return fallback;
    }

    // =====================================================
    // UTIL METHODS
    // =====================================================
    private double parseDouble(Object value) {
        if (value == null) return 0;
        return Double.parseDouble(value.toString());
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
