package com.banking;

import com.banking.dao.AccountDAO;
import com.banking.dao.AccountDAOImpl;
import com.banking.dao.DBConnection;
import com.banking.dao.TransactionDAO;
import com.banking.dao.TransactionDAOImpl;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.service.BankService;
import com.banking.service.BankServiceImpl;

import com.mongodb.client.MongoDatabase;

import java.util.List;
import java.util.Map;

public class TestRunner {
    public static void main(String[] args) {
        try {
            System.out.println("Starting Service Layer Verification...");
            MongoDatabase database = DBConnection.getDatabase();
            AccountDAO accountDAO = new AccountDAOImpl(database);
            TransactionDAO transactionDAO = new TransactionDAOImpl(database);
            BankService service = new BankServiceImpl(accountDAO, transactionDAO);

            // 1. Create
            Customer c = new Customer(0, "John Doe", "555-1234", "john@test.com", "123 Main St");
            String accNum = service.createAccount(c, "SAVINGS", 1000.0);
            System.out.println("Created Account: " + accNum);

            // 2. Deposit
            service.deposit(accNum, 500.0);
            System.out.println("Deposited 500");

            // 3. Withdraw
            service.withdraw(accNum, 200.0);
            System.out.println("Withdrew 200");

            // 4. Balance Enquiry
            Map<String, Object> info = service.getBalance(accNum);
            System.out.println("Balance after operations: " + info.get("balance"));
            if ((Double) info.get("balance") != 1300.0) {
                System.err.println("BALANCE MISMATCH!");
            }

            // 5. Transaction History
            List<Transaction> history = service.getTransactionHistory(accNum);
            System.out.println("History length: " + history.size());
            for (Transaction t : history) {
                System.out.printf(" - %s: %.2f (Balance After: %.2f)\n", 
                    t.getType(), t.getAmount(), t.getBalanceAfter());
            }

            // Verify history order (most recent first)
            if (history.get(0).getType().equals("WITHDRAWAL") && 
                history.get(1).getType().equals("DEPOSIT") && 
                history.get(2).getType().equals("DEPOSIT")) {
                System.out.println("HISTORY ORDER IS CORRECT (Most recent first)");
            } else {
                System.err.println("HISTORY ORDER INCORRECT!");
            }

            System.out.println("Service Layer Verification COMPLETED SUCCESSFULLY.");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
    }
}
