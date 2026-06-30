package com.banking.model;
import java.util.Date;
public class SavingsAccount extends Account {
    private double interestRate;
    public SavingsAccount(String accountNumber, int customerId, double balance, Date openingDate, String status, double interestRate) {
        super(accountNumber, customerId, "SAVINGS", balance, openingDate, status);
        this.interestRate = interestRate;
    }
    @Override
    public double getMinimumBalance() { return 500.0; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}
