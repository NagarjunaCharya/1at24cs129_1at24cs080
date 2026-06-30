package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.exception.InvalidAmountException;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.CurrentAccount;
import com.banking.model.SavingsAccount;
import com.banking.model.Transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the BankService interface.
 * Contains all business logic and validation, delegates persistence
 * to DAO layer. Demonstrates:
 *  - Interface implementation (implements BankService)
 *  - Dependency injection (DAOs injected via constructor)
 *  - Polymorphism (works with abstract Account type; runtime behaviour
 *    differs for SavingsAccount vs CurrentAccount)
 */
public class BankServiceImpl implements BankService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    /**
     * Constructor with dependency injection.
     * @param accountDAO     injected account data access object
     * @param transactionDAO injected transaction data access object
     */
    public BankServiceImpl(AccountDAO accountDAO,
                           TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    // ----------------------------------------------------------------
    // MVP Operation 1: Create Account
    // ----------------------------------------------------------------
    @Override
    public String createAccount(Customer customer, String accountType,
                                double initialDeposit)
            throws InvalidAmountException, Exception {

        // Business rule: initial deposit must be positive
        if (initialDeposit <= 0) {
            throw new InvalidAmountException(
                "Initial deposit must be greater than zero");
        }

        // Build the appropriate Account subclass (polymorphism)
        Account account;
        Date today = new Date();
        if ("CURRENT".equalsIgnoreCase(accountType)) {
            account = new CurrentAccount(
                null, 0, initialDeposit, today, "ACTIVE", 0.0);
        } else {
            account = new SavingsAccount(
                null, 0, initialDeposit, today, "ACTIVE", 4.0);
        }

        // Persist customer and account
        String accountNumber = accountDAO.createAccount(customer, account);

        // Record the initial deposit transaction (atomic balance + push)
        Transaction txn = new Transaction();
        txn.setAccountNumber(accountNumber);
        txn.setType("DEPOSIT");
        txn.setAmount(initialDeposit);
        txn.setBalanceAfter(initialDeposit);
        transactionDAO.recordTransaction(txn);

        return accountNumber;
    }

    // ----------------------------------------------------------------
    // MVP Operation 2: Deposit
    // ----------------------------------------------------------------
    @Override
    public void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException,
                   Exception {

        // Business rule: amount must be positive
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Deposit amount must be greater than zero");
        }

        // Retrieve account — may throw AccountNotFoundException (polymorphic)
        Account account = accountDAO.getAccountByNumber(accountNumber);

        // Update balance in memory
        account.deposit(amount);

        // Record transaction — atomic: sets balance + pushes txn doc
        Transaction txn = new Transaction();
        txn.setAccountNumber(accountNumber);
        txn.setType("DEPOSIT");
        txn.setAmount(amount);
        txn.setBalanceAfter(account.getBalance());
        transactionDAO.recordTransaction(txn);
    }

    // ----------------------------------------------------------------
    // MVP Operation 3: Withdraw
    // ----------------------------------------------------------------
    @Override
    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException,
                   InsufficientBalanceException, Exception {

        // Business rule: amount must be positive
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be greater than zero");
        }

        // Retrieve account — polymorphic: may be SavingsAccount or CurrentAccount
        Account account = accountDAO.getAccountByNumber(accountNumber);

        // account.withdraw() calls getMinimumBalance() — polymorphic dispatch
        account.withdraw(amount);

        // Record transaction — atomic: sets balance + pushes txn doc
        Transaction txn = new Transaction();
        txn.setAccountNumber(accountNumber);
        txn.setType("WITHDRAWAL");
        txn.setAmount(amount);
        txn.setBalanceAfter(account.getBalance());
        transactionDAO.recordTransaction(txn);
    }

    // ----------------------------------------------------------------
    // MVP Operation 4: Balance Enquiry
    // ----------------------------------------------------------------
    @Override
    public Map<String, Object> getBalance(String accountNumber)
            throws AccountNotFoundException, Exception {

        Account account = accountDAO.getAccountByNumber(accountNumber);
        String customerName = accountDAO.getCustomerName(accountNumber);

        Map<String, Object> result = new HashMap<>();
        result.put("balance",      account.getBalance());
        result.put("customerName", customerName);
        result.put("accountType",  account.getAccountType());
        return result;
    }

    // ----------------------------------------------------------------
    // MVP Operation 5: Transaction History
    // ----------------------------------------------------------------
    @Override
    public List<Transaction> getTransactionHistory(String accountNumber)
            throws AccountNotFoundException, Exception {

        // Validate account exists first
        accountDAO.getAccountByNumber(accountNumber);

        return transactionDAO.getTransactionHistory(accountNumber);
    }

    // ----------------------------------------------------------------
    // Phase 2: Fund Transfer (stretch goal)
    // ----------------------------------------------------------------
    @Override
    public void transfer(String fromAccount, String toAccount, double amount)
            throws AccountNotFoundException, InvalidAmountException,
                   InsufficientBalanceException, Exception {

        if (amount <= 0) {
            throw new InvalidAmountException(
                "Transfer amount must be greater than zero");
        }

        // Validate and debit source first
        Account source = accountDAO.getAccountByNumber(fromAccount);
        source.withdraw(amount);

        // Debit source atomically
        Transaction debitTxn = new Transaction();
        debitTxn.setAccountNumber(fromAccount);
        debitTxn.setType("WITHDRAWAL");
        debitTxn.setAmount(amount);
        debitTxn.setBalanceAfter(source.getBalance());
        transactionDAO.recordTransaction(debitTxn);

        // Credit destination
        try {
            Account dest = accountDAO.getAccountByNumber(toAccount);
            dest.deposit(amount);

            Transaction creditTxn = new Transaction();
            creditTxn.setAccountNumber(toAccount);
            creditTxn.setType("DEPOSIT");
            creditTxn.setAmount(amount);
            creditTxn.setBalanceAfter(dest.getBalance());
            transactionDAO.recordTransaction(creditTxn);
        } catch (Exception e) {
            // Roll back source debit if destination write fails
            source.deposit(amount);  // restore in-memory
            Transaction rollback = new Transaction();
            rollback.setAccountNumber(fromAccount);
            rollback.setType("DEPOSIT");
            rollback.setAmount(amount);
            rollback.setBalanceAfter(source.getBalance());
            transactionDAO.recordTransaction(rollback);
            throw e;
        }
    }
}
