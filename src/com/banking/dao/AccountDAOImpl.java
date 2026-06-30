package com.banking.dao;

import com.banking.exception.AccountNotFoundException;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.CurrentAccount;
import com.banking.model.SavingsAccount;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

/**
 * MongoDB implementation of the AccountDAO interface.
 * Uses MongoCollection<Document>, Filters, and Updates builders
 * (never raw string concatenation — the NoSQL-injection-safe equivalent
 * of PreparedStatements).
 */
public class AccountDAOImpl implements AccountDAO {
    private final MongoDatabase database;

    /** Constructor — receives database from DBConnection */
    public AccountDAOImpl(MongoDatabase database) {
        this.database = database;
    }

    // ---- Helper: auto-increment customer ID via a counters collection ----
    private int getNextCustomerId() {
        MongoCollection<Document> counters = database.getCollection("counters");
        Document result = counters.findOneAndUpdate(
            Filters.eq("_id", "customerId"),
            Updates.inc("seq", 1),
            new FindOneAndUpdateOptions()
                .upsert(true)
                .returnDocument(ReturnDocument.AFTER)
        );
        return result.getInteger("seq");
    }

    /**
     * Generates a unique account number: ACC + timestamp + random 3-digit.
     */
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis()
               + (int) (Math.random() * 1000);
    }

    @Override
    public String createAccount(Customer customer, Account account)
            throws Exception {
        // 1. Assign a sequential customer ID and insert customer document
        int customerId = getNextCustomerId();
        customer.setCustomerId(customerId);

        MongoCollection<Document> customers =
            database.getCollection("customers");
        Document customerDoc = new Document("_id", customerId)
            .append("name",    customer.getName())
            .append("phone",   customer.getPhone())
            .append("email",   customer.getEmail())
            .append("address", customer.getAddress());
        customers.insertOne(customerDoc);

        // 2. Generate account number and insert account document
        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);
        account.setCustomerId(customerId);

        Document accountDoc = new Document("_id", accountNumber)
            .append("customerId",   customerId)
            .append("customerName", customer.getName())  // denormalized
            .append("accountType",  account.getAccountType())
            .append("balance",      account.getBalance())
            .append("openingDate",  account.getOpeningDate())
            .append("status",       account.getStatus())
            .append("transactions", new ArrayList<>());

        // Add type-specific fields
        if (account instanceof SavingsAccount) {
            accountDoc.append("interestRate",
                ((SavingsAccount) account).getInterestRate());
        } else if (account instanceof CurrentAccount) {
            accountDoc.append("overdraftLimit",
                ((CurrentAccount) account).getOverdraftLimit());
        }

        MongoCollection<Document> accounts =
            database.getCollection("accounts");
        accounts.insertOne(accountDoc);

        return accountNumber;
    }

    @Override
    public Account getAccountByNumber(String accountNumber)
            throws Exception, AccountNotFoundException {
        MongoCollection<Document> accounts =
            database.getCollection("accounts");
        Document doc = accounts.find(Filters.eq("_id", accountNumber)).first();

        if (doc == null) {
            throw new AccountNotFoundException("Account not found");
        }

        String type = doc.getString("accountType");
        
        // Use Number.class to safely handle both Integer and Double from MongoDB
        Number balanceNum = doc.get("balance", Number.class);
        double balance = balanceNum != null ? balanceNum.doubleValue() : 0.0;
        
        Number custIdNum = doc.get("customerId", Number.class);
        int custId = custIdNum != null ? custIdNum.intValue() : 0;
        
        Date openingDate = doc.getDate("openingDate");
        String status = doc.getString("status");

        if ("SAVINGS".equals(type)) {
            Number rateNum = doc.get("interestRate", Number.class);
            double rate = rateNum != null ? rateNum.doubleValue() : 0.0;
            return new SavingsAccount(
                accountNumber, custId, balance, openingDate, status, rate);
        } else {
            Number limitNum = doc.get("overdraftLimit", Number.class);
            double limit = limitNum != null ? limitNum.doubleValue() : 0.0;
            return new CurrentAccount(
                accountNumber, custId, balance, openingDate, status, limit);
        }
    }

    @Override
    public void updateBalance(String accountNumber, double newBalance)
            throws Exception {
        MongoCollection<Document> accounts =
            database.getCollection("accounts");
        accounts.updateOne(
            Filters.eq("_id", accountNumber),
            Updates.set("balance", newBalance)
        );
    }

    @Override
    public String getCustomerName(String accountNumber) throws Exception {
        MongoCollection<Document> accounts =
            database.getCollection("accounts");
        Document doc = accounts.find(Filters.eq("_id", accountNumber)).first();
        if (doc == null) {
            return null;
        }
        return doc.getString("customerName");
    }
}
