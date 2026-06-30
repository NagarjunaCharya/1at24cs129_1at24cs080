package com.banking.model;
import java.util.Date;
import com.banking.exception.InsufficientBalanceException;
public abstract class Account {
    private String accountNumber;
    private int customerId;
    private String accountType;
    private double balance;
    private Date openingDate;
    private String status;

    public Account(String accountNumber, int customerId, String accountType, double balance, Date openingDate, String status) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.openingDate = openingDate;
        this.status = status;
    }

    public abstract double getMinimumBalance();

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (this.balance - amount < getMinimumBalance()) {
            throw new InsufficientBalanceException("Insufficient balance. Minimum balance must be maintained.");
        }
        this.balance -= amount;
    }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Date getOpeningDate() { return openingDate; }
    public void setOpeningDate(Date openingDate) { this.openingDate = openingDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
