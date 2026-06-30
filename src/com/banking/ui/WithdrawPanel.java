package com.banking.ui;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.exception.InvalidAmountException;
import com.banking.service.BankService;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Panel for withdrawing money from an existing account.
 */
public class WithdrawPanel extends JPanel {
    private BankService bankService;
    private JTextField accountField, amountField;

    public WithdrawPanel(BankService bankService) {
        this.bankService = bankService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Withdraw Money");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);
        gbc.gridwidth = 1;

        // Account number
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Account Number:"), gbc);
        accountField = new JTextField(20);
        gbc.gridx = 1;
        add(accountField, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Amount:"), gbc);
        amountField = new JTextField(20);
        gbc.gridx = 1;
        add(amountField, gbc);

        // Button
        JButton withdrawBtn = new JButton("Withdraw");
        // Default native styling
        withdrawBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        withdrawBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(withdrawBtn, gbc);

        withdrawBtn.addActionListener(e -> handleWithdraw());
    }

    private void handleWithdraw() {
        try {
            String accountNumber = accountField.getText().trim();
            if (accountNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter an account number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountField.getText().trim());
            bankService.withdraw(accountNumber, amount);

            // Fetch updated balance for confirmation
            Map<String, Object> info = bankService.getBalance(accountNumber);
            double newBalance = (Double) info.get("balance");

            JOptionPane.showMessageDialog(this,
                String.format("Withdrawal successful!\nNew Balance: %.2f",
                              newBalance),
                "Success", JOptionPane.INFORMATION_MESSAGE);

            amountField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid number.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidAmountException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Invalid Amount", JOptionPane.ERROR_MESSAGE);
        } catch (AccountNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Account Not Found", JOptionPane.ERROR_MESSAGE);
        } catch (InsufficientBalanceException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
