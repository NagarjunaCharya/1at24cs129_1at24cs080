package com.banking.dao;

import com.banking.exception.AccountNotFoundException;
import com.banking.model.Account;
import com.banking.model.Customer;

/**
 * Data Access Object interface for account persistence operations.
 * Defines the contract for creating, retrieving, and updating accounts.
 * Demonstrates interface-based design (OOP concept).
 */
public interface AccountDAO {

    /**
     * Creates a new customer and account with initial deposit.
     * @param customer the customer details
     * @param account  the account to create
     * @return the generated account number
     * @throws Exception if a database error occurs
     */
    String createAccount(Customer customer, Account account) throws Exception;

    /**
     * Retrieves an account by its account number.
     * @param accountNumber the unique account identifier
     * @return the Account object (SavingsAccount or CurrentAccount)
     * @throws Exception if a database error occurs
     * @throws AccountNotFoundException if no account matches the number
     */
    Account getAccountByNumber(String accountNumber)
        throws Exception, AccountNotFoundException;

    /**
     * Updates the balance of an existing account.
     * @param accountNumber the account to update
     * @param newBalance    the new balance value
     * @throws Exception if a database error occurs
     */
    void updateBalance(String accountNumber, double newBalance) throws Exception;

    /**
     * Retrieves the customer name associated with an account.
     * @param accountNumber the account to look up
     * @return the customer's name
     * @throws Exception if a database error occurs
     */
    String getCustomerName(String accountNumber) throws Exception;
}
