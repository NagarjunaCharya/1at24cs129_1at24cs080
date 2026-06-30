package com.banking;

import com.banking.dao.AccountDAO;
import com.banking.dao.AccountDAOImpl;
import com.banking.dao.DBConnection;
import com.banking.dao.TransactionDAO;
import com.banking.dao.TransactionDAOImpl;
import com.banking.service.BankService;
import com.banking.service.BankServiceImpl;
import com.banking.ui.MainFrame;

import com.mongodb.client.MongoDatabase;

import javax.swing.*;

/**
 * Application entry point.
 * Wires up the layered architecture:
 *   MongoDB → DAO → Service → UI
 * and launches the Swing GUI on the Event Dispatch Thread.
 */
public class Main {

    public static void main(String[] args) {
        // Global uncaught-exception handler (Req 9.8 fallback)
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            JOptionPane.showMessageDialog(null,
                "Unexpected error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        });

        // Set a nicer look-and-feel if available
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Database connection
                MongoDatabase database = DBConnection.getDatabase();

                // 2. DAO layer (dependency injection)
                AccountDAO accountDAO = new AccountDAOImpl(database);
                TransactionDAO transactionDAO = new TransactionDAOImpl(database);

                // 3. Service layer (dependency injection)
                BankService bankService =
                    new BankServiceImpl(accountDAO, transactionDAO);

                // 4. UI layer
                MainFrame frame = new MainFrame(bankService);
                frame.setVisible(true);

                // Close MongoDB client on window close
                frame.addWindowListener(
                    new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(
                                java.awt.event.WindowEvent e) {
                            DBConnection.close();
                        }
                    }
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                    "Failed to start application:\n" + ex.getMessage(),
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                System.exit(1);
            }
        });
    }
}
