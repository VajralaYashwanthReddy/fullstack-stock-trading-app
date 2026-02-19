package com.trading.service;

import com.trading.dto.*;
import com.trading.model.*;
import com.trading.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradingService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockService stockService;

    public TradingService(UserRepository userRepository,
                          TransactionRepository transactionRepository,
                          PortfolioRepository portfolioRepository,
                          StockService stockService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.portfolioRepository = portfolioRepository;
        this.stockService = stockService;
    }

    // =====================================================
    // ✅ BUY STOCK
    // =====================================================
    @Transactional
    public TradeResponse buyStock(TradeRequest request) {

        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        String symbol = request.getSymbol().toUpperCase();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        double price = stockService.getQuote(symbol).getCurrentPrice();
        double total = price * request.getQuantity();

        if (user.getBalance() < total) {
            throw new RuntimeException("Insufficient balance");
        }

        // 🔥 Deduct wallet
        user.setBalance(user.getBalance() - total);
        userRepository.save(user);

        // 🔥 Save transaction
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setSymbol(symbol);
        transaction.setType("BUY");
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(price);
        transaction.setTotalAmount(total);
        transaction.setTransactionTime(LocalDateTime.now());

        transactionRepository.save(transaction);

        // 🔥 Update Portfolio
        Portfolio portfolio = portfolioRepository
                .findByUserIdAndSymbol(user.getId(), symbol)
                .orElse(null);

        if (portfolio == null) {
            portfolio = new Portfolio();
            portfolio.setUserId(user.getId());
            portfolio.setSymbol(symbol);
            portfolio.setQuantity(request.getQuantity());
            portfolio.setAveragePrice(price);
        } else {

            int newQty = portfolio.getQuantity() + request.getQuantity();

            double newAvg =
                    ((portfolio.getQuantity() * portfolio.getAveragePrice())
                            + (request.getQuantity() * price))
                            / newQty;

            portfolio.setQuantity(newQty);
            portfolio.setAveragePrice(newAvg);
        }

        portfolioRepository.save(portfolio);

        return new TradeResponse(
                "Stock purchased successfully",
                price,
                total,
                user.getBalance()
        );
    }

    // =====================================================
    // 🔴 SELL STOCK
    // =====================================================
    @Transactional
    public TradeResponse sellStock(TradeRequest request) {

        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        String symbol = request.getSymbol().toUpperCase();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Portfolio portfolio = portfolioRepository
                .findByUserIdAndSymbol(user.getId(), symbol)
                .orElseThrow(() -> new RuntimeException("Stock not owned"));

        if (portfolio.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough quantity to sell");
        }

        double price = stockService.getQuote(symbol).getCurrentPrice();
        double total = price * request.getQuantity();

        // 🔥 Add money to wallet
        user.setBalance(user.getBalance() + total);
        userRepository.save(user);

        // 🔥 Save transaction
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setSymbol(symbol);
        transaction.setType("SELL");
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(price);
        transaction.setTotalAmount(total);
        transaction.setTransactionTime(LocalDateTime.now());

        transactionRepository.save(transaction);

        // 🔥 Update portfolio
        int remainingQty = portfolio.getQuantity() - request.getQuantity();

        if (remainingQty == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolio.setQuantity(remainingQty);
            portfolioRepository.save(portfolio);
        }

        return new TradeResponse(
                "Stock sold successfully",
                price,
                total,
                user.getBalance()
        );
    }

    // =====================================================
    // 📊 GET PORTFOLIO (WITH LIVE PROFIT/LOSS)
    // =====================================================
    public List<PortfolioResponse> getPortfolio(Long userId) {

        return portfolioRepository.findByUserId(userId)
                .stream()
                .map(p -> {

                    double livePrice =
                            stockService.getQuote(p.getSymbol())
                                    .getCurrentPrice();

                    double invested =
                            p.getAveragePrice() * p.getQuantity();

                    double currentValue =
                            livePrice * p.getQuantity();

                    double profitLoss =
                            currentValue - invested;

                    return new PortfolioResponse(
                            p.getSymbol(),
                            p.getQuantity(),
                            livePrice,
                            currentValue,
                            p.getAveragePrice(),
                            profitLoss
                    );
                })
                .collect(Collectors.toList());
    }

    // =====================================================
    // 📜 GET TRANSACTIONS
    // =====================================================
    public List<Transaction> getTransactions(Long userId) {
        return transactionRepository
                .findByUserIdOrderByTransactionTimeDesc(userId);
    }
}
