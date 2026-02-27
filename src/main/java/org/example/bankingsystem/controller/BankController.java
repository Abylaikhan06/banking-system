package org.example.bankingsystem.controller;

import org.example.bankingsystem.dto.AmountRequest;
import org.example.bankingsystem.model.Account;
import org.example.bankingsystem.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class BankController {

    private final Map<Long, Customer> customers = new HashMap<>();
    private final Map<Long, Account> accounts = new HashMap<>();

    private final AtomicLong customerSeq = new AtomicLong(1);
    private final AtomicLong accountSeq = new AtomicLong(1);

    // 1) CREATE CUSTOMER
    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        long id = customerSeq.getAndIncrement();
        customer.setId(id);
        customers.put(id, customer);
        return ResponseEntity.ok(customer);
    }

    // 2) CREATE ACCOUNT
    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        if (account.getCustomerId() == null || !customers.containsKey(account.getCustomerId())) {
            return ResponseEntity.badRequest().body("Customer not found");
        }
        long id = accountSeq.getAndIncrement();
        account.setId(id);
        if (account.getBalance() < 0) account.setBalance(0);
        accounts.put(id, account);
        return ResponseEntity.ok(account);
    }

    // 3) GET ACCOUNT BY ID
    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        Account acc = accounts.get(id);
        if (acc == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(acc);
    }

    // 4) DEPOSIT
    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id, @RequestBody AmountRequest req) {
        Account acc = accounts.get(id);
        if (acc == null) return ResponseEntity.notFound().build();
        if (req.getAmount() <= 0) return ResponseEntity.badRequest().body("Amount must be > 0");

        acc.setBalance(acc.getBalance() + req.getAmount());
        return ResponseEntity.ok(acc);
    }

    // 5) WITHDRAW
    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id, @RequestBody AmountRequest req) {
        Account acc = accounts.get(id);
        if (acc == null) return ResponseEntity.notFound().build();
        if (req.getAmount() <= 0) return ResponseEntity.badRequest().body("Amount must be > 0");
        if (acc.getBalance() < req.getAmount()) return ResponseEntity.badRequest().body("Not enough balance");

        acc.setBalance(acc.getBalance() - req.getAmount());
        return ResponseEntity.ok(acc);
    }
}