package com.banking.dao;

import com.banking.model.Transaction;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.PushOptions;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * MongoDB implementation of the TransactionDAO interface.
 * Transactions are embedded in the account document for atomic updates:
 * a single updateOne call sets balance AND pushes the transaction.
 */
public class TransactionDAOImpl implements TransactionDAO {
    private final MongoDatabase database;

    /** Constructor — receives database from DBConnection */
    public TransactionDAOImpl(MongoDatabase database) {
        this.database = database;
    }

    /**
     * Atomically updates the account balance AND pushes the transaction
     * into the embedded transactions array (newest at position 0).
     * This single-document update is atomic without a replica set.
     */
    @Override
    public void recordTransaction(Transaction transaction) throws Exception {
        MongoCollection<Document> accounts =
            database.getCollection("accounts");

        // Build the transaction sub-document
        Document txnDoc = new Document("type", transaction.getType())
            .append("amount",       transaction.getAmount())
            .append("balanceAfter", transaction.getBalanceAfter())
            .append("txnTime",     new Date());

        // Atomic: set balance + push transaction to front of array
        accounts.updateOne(
            Filters.eq("_id", transaction.getAccountNumber()),
            Updates.combine(
                Updates.set("balance", transaction.getBalanceAfter()),
                Updates.pushEach("transactions",
                    Arrays.asList(txnDoc),
                    new PushOptions().position(0))
            )
        );
    }

    /**
     * Reads the embedded transactions array from the account document.
     * Already stored newest-first (position 0) so no re-sorting needed.
     */
    @Override
    public List<Transaction> getTransactionHistory(String accountNumber)
            throws Exception {
        MongoCollection<Document> accounts =
            database.getCollection("accounts");
        Document doc = accounts.find(
            Filters.eq("_id", accountNumber)).first();

        List<Transaction> history = new ArrayList<>();
        if (doc == null) {
            return history;
        }

        List<Document> txnDocs = doc.getList("transactions",
                                              Document.class);
        if (txnDocs == null) {
            return history;
        }

        int idCounter = txnDocs.size();
        for (Document t : txnDocs) {
            Transaction txn = new Transaction();
            txn.setTransactionId(idCounter--);
            txn.setAccountNumber(accountNumber);
            txn.setType(t.getString("type"));
            Number amtNum = t.get("amount", Number.class);
            txn.setAmount(amtNum != null ? amtNum.doubleValue() : 0.0);
            
            Number balNum = t.get("balanceAfter", Number.class);
            txn.setBalanceAfter(balNum != null ? balNum.doubleValue() : 0.0);

            Date txnTime = t.getDate("txnTime");
            if (txnTime != null) {
                txn.setTxnTime(new Timestamp(txnTime.getTime()));
            }
            history.add(txn);
        }
        return history;
    }
}
