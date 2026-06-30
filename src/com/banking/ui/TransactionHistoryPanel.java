package com.banking.ui;

import com.banking.exception.AccountNotFoundException;
import com.banking.model.Transaction;
import com.banking.service.BankService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for viewing transaction history of an account.
 * Displays transactions in a JTable, most recent first,
 * with amounts formatted to 2 decimal places.
 */
public class TransactionHistoryPanel extends JPanel {
    private BankService bankService;
    private JTextField accountField;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public TransactionHistoryPanel(BankService bankService) {
        this.bankService = bankService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ---- Top panel: input + button ----
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(title);

        topPanel.add(new JLabel("Account Number:"));
        accountField = new JTextField(18);
        topPanel.add(accountField);

        JButton viewBtn = new JButton("View History");
        // Default native styling
        viewBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        viewBtn.setFocusPainted(false);
        topPanel.add(viewBtn);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(titlePanel, BorderLayout.NORTH);
        northPanel.add(topPanel,   BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // ---- Table ----
        String[] columns = {"#", "Type", "Amount", "Balance After", "Timestamp"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;  // read-only table
            }
        };
        historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Monospaced", Font.PLAIN, 13));
        historyTable.setRowHeight(24);
        add(new JScrollPane(historyTable), BorderLayout.CENTER);

        viewBtn.addActionListener(e -> handleViewHistory());
    }

    private void handleViewHistory() {
        try {
            String accountNumber = accountField.getText().trim();
            if (accountNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter an account number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Transaction> transactions =
                bankService.getTransactionHistory(accountNumber);

            // Clear existing rows
            tableModel.setRowCount(0);

            if (transactions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No transactions found for this account.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Populate table — already ordered most-recent-first
            for (Transaction txn : transactions) {
                tableModel.addRow(new Object[]{
                    txn.getTransactionId(),
                    txn.getType(),
                    String.format("%.2f", txn.getAmount()),
                    String.format("%.2f", txn.getBalanceAfter()),
                    txn.getTxnTime() != null
                        ? txn.getTxnTime().toString() : "N/A"
                });
            }

        } catch (AccountNotFoundException ex) {
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Account Not Found", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
