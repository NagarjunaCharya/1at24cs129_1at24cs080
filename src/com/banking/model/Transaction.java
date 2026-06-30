package com.banking.model;
import java.util.Date;
import java.sql.Timestamp;
public class Transaction {
    private int transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private Timestamp txnTime;
    private String description;

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
    public Timestamp getTxnTime() { return txnTime; }
    public void setTxnTime(Timestamp txnTime) { this.txnTime = txnTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
