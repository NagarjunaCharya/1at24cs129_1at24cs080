package com.banking.dao;

import com.banking.model.Transaction;
import java.util.List;

/**
 * Data Access Object interface for transaction persistence operations.
 * Defines the contract for recording and retrieving transactions.
 * Demonstrates interface-based design (OOP concept).
 */
public interface TransactionDAO {

    /**
     * Records a new transaction.
     * For MongoDB: atomically pushes the transaction into the account
     * document's embedded transactions array and updates balance.
     * @param transaction the transaction to record
     * @throws Exception if a database error occurs
     */
    void recordTransaction(Transaction transaction) throws Exception;

    /**
     * Retrieves all transactions for an account, most recent first.
     * @param accountNumber the account to query
     * @return list of transactions ordered by timestamp descending
     * @throws Exception if a database error occurs
     */
    List<Transaction> getTransactionHistory(String accountNumber) throws Exception;
}
