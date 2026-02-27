package org.example.bankingsystem.model;

public class Account {

    private Long id;
    private Long customerId;
    private double balance;

    public Account() {
    }

    public Account(Long id, Long customerId, double balance) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}