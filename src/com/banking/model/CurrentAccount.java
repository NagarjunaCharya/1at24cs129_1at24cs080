package com.banking.model;
import java.util.Date;
public class CurrentAccount extends Account {
    private double overdraftLimit;
    public CurrentAccount(String accountNumber, int customerId, double balance, Date openingDate, String status, double overdraftLimit) {
        super(accountNumber, customerId, "CURRENT", balance, openingDate, status);
        this.overdraftLimit = overdraftLimit;
    }
    @Override
    public double getMinimumBalance() { return 1000.0 - overdraftLimit; }
    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }
}
