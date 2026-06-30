package com.banking;

import com.banking.dao.AccountDAO;
import com.banking.dao.AccountDAOImpl;
import com.banking.dao.DBConnection;
import com.banking.dao.TransactionDAO;
import com.banking.dao.TransactionDAOImpl;
import com.banking.exception.InsufficientBalanceException;
import com.banking.exception.InvalidAmountException;
import com.banking.model.Customer;
import com.banking.service.BankService;
import com.banking.service.BankServiceImpl;
import com.mongodb.client.MongoDatabase;

import java.util.Map;

/**
 * Comprehensive Test Suite for the Banking Application.
 * Tests MVP features and Phase 2 Transfer logic.
 */
public class BankingTestSuite {

    private static BankService service;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      RUNNING BANKING TEST SUITE          ");
        System.out.println("==========================================");
        
        try {
            MongoDatabase database = DBConnection.getDatabase();
            AccountDAO accountDAO = new AccountDAOImpl(database);
            TransactionDAO transactionDAO = new TransactionDAOImpl(database);
            service = new BankServiceImpl(accountDAO, transactionDAO);

            int passed = 0;
            int failed = 0;

            if (testCreateAccount()) passed++; else failed++;
            if (testDepositAndWithdraw()) passed++; else failed++;
            if (testInsufficientBalance()) passed++; else failed++;
            if (testInvalidAmount()) passed++; else failed++;
            if (testPhase2FundTransfer()) passed++; else failed++;

            System.out.println("==========================================");
            System.out.println("TEST RESULTS: " + passed + " Passed, " + failed + " Failed.");
            System.out.println("==========================================");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
    }

    private static boolean testCreateAccount() {
        try {
            System.out.print("Test - Create Account: ");
            Customer c = new Customer(0, "Alice Smith", "111-2222", "alice@test.com", "123 Apple St");
            String accNum = service.createAccount(c, "SAVINGS", 1000.0);
            
            Map<String, Object> info = service.getBalance(accNum);
            if ((Double) info.get("balance") == 1000.0 && "Alice Smith".equals(info.get("customerName"))) {
                System.out.println("PASSED");
                return true;
            }
        } catch (Exception e) {
            System.out.println("FAILED (" + e.getMessage() + ")");
        }
        System.out.println("FAILED");
        return false;
    }

    private static boolean testDepositAndWithdraw() {
        try {
            System.out.print("Test - Deposit & Withdraw: ");
            Customer c = new Customer(0, "Bob Jones", "333-4444", "bob@test.com", "456 Banana Ave");
            String accNum = service.createAccount(c, "CURRENT", 2000.0);
            
            service.deposit(accNum, 500.0);
            service.withdraw(accNum, 1000.0);
            
            Map<String, Object> info = service.getBalance(accNum);
            if ((Double) info.get("balance") == 1500.0 && service.getTransactionHistory(accNum).size() == 3) {
                System.out.println("PASSED");
                return true;
            }
        } catch (Exception e) {
            System.out.println("FAILED (" + e.getMessage() + ")");
        }
        System.out.println("FAILED");
        return false;
    }

    private static boolean testInsufficientBalance() {
        try {
            System.out.print("Test - Insufficient Balance (Savings Rule): ");
            Customer c = new Customer(0, "Charlie Brown", "555-6666", "charlie@test.com", "789 Cherry Blvd");
            String accNum = service.createAccount(c, "SAVINGS", 600.0);
            
            try {
                // Minimum balance for Savings is 500, so withdrawing 200 should fail
                service.withdraw(accNum, 200.0);
                System.out.println("FAILED (Expected exception was not thrown)");
                return false;
            } catch (InsufficientBalanceException e) {
                System.out.println("PASSED");
                return true;
            }
        } catch (Exception e) {
            System.out.println("FAILED (" + e.getMessage() + ")");
        }
        return false;
    }

    private static boolean testInvalidAmount() {
        try {
            System.out.print("Test - Invalid Negative Amount: ");
            Customer c = new Customer(0, "Diana Prince", "777-8888", "diana@test.com", "101 Date St");
            String accNum = service.createAccount(c, "SAVINGS", 1000.0);
            
            try {
                service.deposit(accNum, -50.0);
                System.out.println("FAILED (Expected exception was not thrown)");
                return false;
            } catch (InvalidAmountException e) {
                System.out.println("PASSED");
                return true;
            }
        } catch (Exception e) {
            System.out.println("FAILED (" + e.getMessage() + ")");
        }
        return false;
    }

    private static boolean testPhase2FundTransfer() {
        try {
            System.out.print("Test - Phase 2 Fund Transfer: ");
            Customer c1 = new Customer(0, "Eve Adams", "999-0000", "eve@test.com", "202 Fig Way");
            String srcAcc = service.createAccount(c1, "SAVINGS", 2000.0);
            
            Customer c2 = new Customer(0, "Frank Castle", "000-1111", "frank@test.com", "303 Grape Cir");
            String destAcc = service.createAccount(c2, "CURRENT", 1000.0);
            
            // Transfer 500 from Eve to Frank
            service.transfer(srcAcc, destAcc, 500.0);
            
            double srcBalance = (Double) service.getBalance(srcAcc).get("balance");
            double destBalance = (Double) service.getBalance(destAcc).get("balance");
            
            if (srcBalance == 1500.0 && destBalance == 1500.0) {
                System.out.println("PASSED");
                return true;
            }
        } catch (Exception e) {
            System.out.println("FAILED (" + e.getMessage() + ")");
        }
        System.out.println("FAILED");
        return false;
    }
}
