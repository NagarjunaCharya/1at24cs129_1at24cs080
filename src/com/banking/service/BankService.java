package com.banking.service;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.exception.InvalidAmountException;
import com.banking.model.Customer;
import com.banking.model.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Service layer interface defining all banking business operations.
 * Demonstrates interface-based design (OOP concept):
 * the UI layer depends on this contract, not the implementation.
 */
public interface BankService {

    /**
     * Creates a new customer account with an initial deposit.
     * @param customer       customer details
     * @param accountType    "SAVINGS" or "CURRENT"
     * @param initialDeposit amount for the opening deposit
     * @return the generated account number
     */
    String createAccount(Customer customer, String accountType,
                         double initialDeposit)
        throws InvalidAmountException, Exception;

    /**
     * Deposits money into an existing account.
     * @param accountNumber the target account
     * @param amount        the deposit amount (must be > 0)
     */
    void deposit(String accountNumber, double amount)
        throws AccountNotFoundException, InvalidAmountException, Exception;

    /**
     * Withdraws money from an existing account.
     * @param accountNumber the target account
     * @param amount        the withdrawal amount (must be > 0)
     */
    void withdraw(String accountNumber, double amount)
        throws AccountNotFoundException, InvalidAmountException,
               InsufficientBalanceException, Exception;

    /**
     * Retrieves account balance with customer details.
     * @param accountNumber the account to look up
     * @return Map with keys: "balance", "customerName", "accountType"
     */
    Map<String, Object> getBalance(String accountNumber)
        throws AccountNotFoundException, Exception;

    /**
     * Retrieves transaction history for an account, most recent first.
     * @param accountNumber the account to query
     * @return list of transactions
     */
    List<Transaction> getTransactionHistory(String accountNumber)
        throws AccountNotFoundException, Exception;

    /**
     * Transfers funds between two accounts (Phase 2).
     */
    void transfer(String fromAccount, String toAccount, double amount)
        throws AccountNotFoundException, InvalidAmountException,
               InsufficientBalanceException, Exception;
}
